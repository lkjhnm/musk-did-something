package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.SnsType;
import com.choi.notice.service.sns.twitter.entity.Tweet;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.PostConstruct;

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
		elonmusk.flatMap(twitterApiService::validateInfluence)
				.flatMap(subscribeRepository::save)
				.block();
	}

	private void cleanDb() {
		this.subscribeRepository.deleteAll().block();
	}

	Mono<Influence> elonmusk = Mono.just(new Influence("elonmusk", SnsType.twitter));

	@Test
	public void tweetCheckTest() {
		elonmusk
				.map(influence -> influence.getId())
				.flatMap(subscribeRepository::findByInfluenceId)
				.map(subscribe -> subscribe.getInfluence().<TwitterUser>getSnsDetail())
				.flatMap(twitterApiService::checkTweet)
				.log()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

}
