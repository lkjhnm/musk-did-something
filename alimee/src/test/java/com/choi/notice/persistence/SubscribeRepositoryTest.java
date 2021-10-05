package com.choi.notice.persistence;

import com.choi.notice.service.sns.SnsType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class SubscribeRepositoryTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SubscribeRepository subscribeRepository;


	private Sinks.Many<Influence> influenceEmitter = Sinks.many()
	                                                .unicast()
	                                                .onBackpressureBuffer();

	private Flux<Influence> influencePublisher = influenceEmitter.asFlux().log();

	/**
	 *  Subscribe(구독 정보)가 발행되면 Influence 기준으로 Subscribe 를 찾은 후,신규 구독자 아이디를 추가한 후 저장 (update)
	 */
	@Test
	public void subscribeSaveTest() {
		Flux<Subscribe> subscribePublisher = findSubscribeByInfluence(influencePublisher);
		publishInfluence();

		Flux<Subscribe> savedSubscribePublisher = this.subscribeRepository.saveAll(subscribePublisher);
		StepVerifier.create(savedSubscribePublisher)
				.assertNext(subscribe -> {
					assertThat(subscribe.getUserId()).contains("test@naver.com");
					assertThat(subscribe.getInfluence().getId()).isEqualTo("test");
				})
				.verifyComplete();
	}

	private Flux<Subscribe> findSubscribeByInfluence(Flux<Influence> influencePublisher) {
		Flux<Subscribe> subscribePublisher = influencePublisher
				.flatMap(influence -> this.subscribeRepository.findByInfluence(influence)
				                                              .defaultIfEmpty(new Subscribe(Collections.emptyList(), influence))
				)
				.log()
				.map(subscribe -> subscribe.addUserId("test@naver.com"));
		return subscribePublisher;
	}

	private void publishInfluence() {
		this.influenceEmitter.tryEmitNext(new Influence("test", SnsType.twitter));
		this.influenceEmitter.tryEmitComplete();
	}
}
