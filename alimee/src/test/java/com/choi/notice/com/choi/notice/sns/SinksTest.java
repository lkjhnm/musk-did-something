package com.choi.notice.com.choi.notice.sns;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class SinksTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void sinkTest() throws InterruptedException {
		Sinks.Many<String> objectMany = Sinks.many()
		                                     .multicast()
		                                     .onBackpressureBuffer();
		Flux<String> stringFlux = objectMany.asFlux().log();
		tryEmitInterval(objectMany);

		StepVerifier.create(stringFlux)
		            .expectNext("test1")
					.expectNext("test2")
					.verifyComplete();
	}

	private void tryEmitInterval(Sinks.Many<String> objectMany) throws InterruptedException {
		objectMany.tryEmitNext("test1");
		Thread.sleep(Duration.ofSeconds(1).toMillis());
		objectMany.tryEmitNext("test2");
		objectMany.tryEmitComplete();
	}
}
