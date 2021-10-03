package com.choi.notice.entity;

import com.choi.notice.sns.SnsType;
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
	@Test
	public void subscribeListSaveTest() {
		Flux<Influence> influencePublisher = influenceEmitter.asFlux().log();
		Flux<Subscribe> subscribePublisher = findSubscribeByInflunce(influencePublisher);
		publishInfluence();

		Flux<Subscribe> savedSubscribePublisher = this.subscribeRepository.saveAll(subscribePublisher);
		StepVerifier.create(savedSubscribePublisher)
				.assertNext(subscribe -> {
					assertThat(subscribe.getUserMail()).contains("test@naver.com");
					assertThat(subscribe.getInfluence().getName()).isEqualTo("test");
				})
				.verifyComplete();
	}

	private Flux<Subscribe> findSubscribeByInflunce(Flux<Influence> influencePublisher) {
		Flux<Subscribe> subscribePublisher = influencePublisher
				.flatMap(influence -> this.subscribeRepository.findByInfluence(influence)
				                                              .defaultIfEmpty(new Subscribe(Collections.emptyList(), influence))
				)
				.log()
				.map(subscribe -> subscribe.addUserMail("test2@naver.com"));
		return subscribePublisher;
	}

	private void publishInfluence() {
		this.influenceEmitter.tryEmitNext(new Influence("test", SnsType.twitter));
		this.influenceEmitter.tryEmitComplete();
	}
}
