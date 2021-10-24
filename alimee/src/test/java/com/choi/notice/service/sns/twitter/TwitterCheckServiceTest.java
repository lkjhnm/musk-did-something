package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.Subscribe;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.SnsType;
import com.choi.notice.service.sns.twitter.entity.Tweet;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;

/**
 *  1. 트위터 체크
 *  2. 최신 트윗 여부 판별
 */
public class TwitterCheckServiceTest extends AbstractTwitterServiceTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SubscribeRepository subscribeRepository;

	@Autowired
	private TwitterApiService twitterApiService;

	@PostConstruct
	public void initialize() {
		cleanDb();
		Mono.just(createTestData())
				.flatMap(subscribeRepository::save)
				.block();
	}

	private void cleanDb() {
		this.subscribeRepository.deleteAll().block();
	}

	private Subscribe createTestData() {
		TwitterUser twitterUser = new TwitterUser();
		Tweet tweet = new Tweet();
		Tweet.Meta meta = new Tweet.Meta();
		meta.setNewestId("11111");
		meta.setOldestId("11111");
		tweet.setMeta(meta);

		TwitterUser.Profile profile = new TwitterUser.Profile().setId("44196397").setName("Elon Musk").setUsername("elonmusk");
		twitterUser.setProfile(profile);
		twitterUser.setTweet(tweet);
		return new Subscribe(Collections.emptyList(), new Influence("elonmusk", SnsType.twitter).setSnsDetail(twitterUser));
	}

	Mono<Influence> elonmusk = Mono.just(new Influence("elonmusk", SnsType.twitter));

	@Test
	public void tweetApiTest() {
		elonmusk
				.map(influence -> influence.getId())
				.flatMap(subscribeRepository::findByInfluenceId)
				.map(subscribe -> subscribe.getInfluence().<TwitterUser>getSnsDetail())
				.map(this.twitterApiService::getTweet)
				.log()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	public void checkTweetFrequentlyTest() {
		Flux.interval(Duration.ofSeconds(1))
				.take(6) // 5번의 스케줄링을 테스트
				.doOnNext(aLong -> {
					if (aLong == 4l) {
						throw new RuntimeException("test");
					}
				})
				.flatMap(unused -> subscribeRepository.findAll())
	            .flatMap(this::checkNewTweetPost)
				.onErrorContinue((throwable, o) -> logger.error("exception occurred during tweet check, Element : {}", o))
	            .log()
	            .as(StepVerifier::create)
	            .expectNextCount(5)
	            .verifyComplete();
	}

	@Test
	public void updateNewTweetTest() {
		Sinks.Many<Subscribe> subscribeEmitter = Sinks.many().multicast().onBackpressureBuffer();
		Flux<Subscribe> updatePublisher = subscribeEmitter.asFlux();

		Flux.interval(Duration.ofSeconds(1))
		    .take(1)
		    .flatMap(unused -> subscribeRepository.findAll())
			.log()
		    .flatMap(this::checkNewTweetPost)
		    .subscribe(subscribe -> {
			    subscribeEmitter.tryEmitNext(subscribe);
			    subscribeEmitter.tryEmitComplete();
		    });

		updatePublisher
				.log()
				.flatMap(subscribe -> subscribeRepository.save(subscribe))
				.onErrorContinue((throwable, o) -> logger.error("exception occurred during update tweet, Element : {}", o))
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	private Mono<Subscribe> checkNewTweetPost(Subscribe subscribe) {
		TwitterUser fetched = subscribe.getInfluence().getSnsDetail();
		return twitterApiService.getTweet(fetched)
		                        .map(tweet -> Tuples.of(fetched.getTweet(), tweet))
		                        .filter(this::compareTweet)
		                        .map(tuple -> tuple.getT2())
		                        .map(newest -> {
			                        subscribe.getInfluence().<TwitterUser>getSnsDetail().setTweet(newest);
			                        return subscribe;
		                        });
	}

	private boolean compareTweet(Tuple2<Tweet,Tweet> tuple) {
		return !tuple.getT1().getRecentlyTweetId().equals(tuple.getT2().getRecentlyTweetId());
	}
}
