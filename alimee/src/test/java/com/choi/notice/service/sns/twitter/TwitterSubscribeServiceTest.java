package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.Subscribe;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.SnsType;
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
	public void test() {
		this.subscribeRepository.deleteAll().block();
	}

	Mono<Influence> testPublisher = Mono.just(new Influence("elonmusk", SnsType.twitter));

	@Test
	public void fetchSubscribeByInfluence() {
		testPublisher
				.flatMap(this::findSubscribeByInfluence)
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	public void saveSubscribeTest() {
		testPublisher
				.log()
				.flatMap(this::findSubscribeByInfluence)
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

	private Mono<Subscribe> findSubscribeByInfluence(Influence influence) {
		return this.subscribeRepository.findByInfluenceId(influence.getId())
		                               .switchIfEmpty(twitterApiService.validateInfluence(influence));
	}
}
