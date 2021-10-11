package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.Subscribe;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.SnsType;
import com.choi.notice.service.sns.twitter.entity.Tweet;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 *  1. 트위터 체크
 *  2. 최신 트윗 여부 판별
 */
public class TwitterCheckServiceTest extends AbstractTwitterServiceTest {

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
	public void tweetCheckTest() {
		elonmusk
				.map(influence -> influence.getId())
				.flatMap(subscribeRepository::findByInfluenceId)
				.map(subscribe -> subscribe.getInfluence().<TwitterUser>getSnsDetail())
				.flatMap(this::zipFetchedTweetAndRecentlyTweet)
				.filter(this::checkPostNewTweet)
				.log()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	private Mono<Tuple2<Tweet, Tweet>> zipFetchedTweetAndRecentlyTweet(TwitterUser twitterUser) {
		return twitterApiService.getTweet(twitterUser)
		                        .map(tweet -> Tuples.of(twitterUser.getTweet(), tweet));
	}

	private boolean checkPostNewTweet(Tuple2<Tweet,Tweet> tuple) {
		return !tuple.getT1().getRecentlyTweetId().equals(tuple.getT2().getRecentlyTweetId());
	}
}
