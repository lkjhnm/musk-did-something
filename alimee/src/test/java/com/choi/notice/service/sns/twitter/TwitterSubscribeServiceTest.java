package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.Subscribe;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.SnsType;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.PostConstruct;

/**
 *
 *  1. 인플루언서 구독요청 감지
 *  2. 인플루언서 조회 없으면 인플루언서 유효성 검사
 *  3. 신규 인플루언서 구독목록에 추가
 */
public class TwitterSubscribeServiceTest extends AbstractTwitterServiceTest {

	@Autowired
	private SubscribeRepository subscribeRepository;

	@Autowired
	private TwitterApiService twitterApiService;

	@PostConstruct
	public void initialize() {
		cleanDb();
	}

	private void cleanDb() {
		this.subscribeRepository.deleteAll().block();
	}

	Mono<Influence> testPublisher = Mono.just(new Influence("elonmusk", SnsType.twitter));

	@Test
	public void fetchSubscribeByInfluence() {
		testPublisher
				.flatMap(this::getSubscribeOrElseGetNewOne)
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	public void tweetJsonBindTest() {
		testPublisher
				.flatMap(this::validateAndGetSubscribe)
				.log()
				.as(StepVerifier::create)
				.assertNext(subscribe -> {
					Assertions.assertNotNull(subscribe.getInfluence().<TwitterUser>getSnsDetail().getProfile());
					Assertions.assertNotNull(subscribe.getInfluence().<TwitterUser>getSnsDetail().getTweet());
					Assertions.assertNotNull(subscribe.getInfluence().<TwitterUser>getSnsDetail().getTweet().getData());
				})
				.verifyComplete();
	}

	@Test
	public void saveSubscribeTest() {
		testPublisher
				.flatMap(this::getSubscribeOrElseGetNewOne)
				.flatMap(subscribeRepository::save)
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();

		testPublisher
				.map(influence -> influence.getId())
				.map(subscribeRepository::findByInfluenceId)
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	private Mono<Subscribe> getSubscribeOrElseGetNewOne(Influence influence) {
		return this.subscribeRepository.findByInfluenceId(influence.getId())
		                               .switchIfEmpty(validateAndGetSubscribe(influence));
	}

	public Mono<Subscribe> validateAndGetSubscribe(Influence influence) {
		return twitterApiService.validateInfluence(influence)
		                        .flatMap(twitterUser -> twitterUser.isError() ?
				                        Mono.error(new RuntimeException("this is unknown user")) :
				                        createNewSubscribe(twitterUser, influence));
	}

	private Mono<Subscribe> createNewSubscribe(TwitterUser twitterUser, Influence influence) {
		return twitterApiService.getTweet(twitterUser)
		                 .map(tweet -> twitterUser.setTweet(tweet))
		                 .map(twu -> new Subscribe(influence.setSnsDetail(twu)));
	}
}
