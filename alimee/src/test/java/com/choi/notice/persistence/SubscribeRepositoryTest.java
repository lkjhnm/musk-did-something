package com.choi.notice.persistence;

import com.choi.notice.boot.config.MongoDbConfiguration;
import com.choi.notice.service.sns.SnsType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = { MongoDbConfiguration.class })
@EnableAutoConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
public class SubscribeRepositoryTest {

	@Autowired
	private SubscribeRepository subscribeRepository;

	private Sinks.Many<Influence> influenceEmitter = Sinks.many().unicast().onBackpressureBuffer();
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
					assertThat(subscribe.getInfluence().getId()).isEqualTo("test");
				})
				.verifyComplete();
	}

	private Flux<Subscribe> findSubscribeByInfluence(Flux<Influence> influencePublisher) {
		Flux<Subscribe> subscribePublisher = influencePublisher
				.flatMap(influence -> this.subscribeRepository.findByInfluenceId(influence.getId())
				                                              .defaultIfEmpty(new Subscribe(influence))
				)
				.log();
		return subscribePublisher;
	}

	private void publishInfluence() {
		this.influenceEmitter.tryEmitNext(new Influence("test", SnsType.twitter));
		this.influenceEmitter.tryEmitComplete();
	}
}
