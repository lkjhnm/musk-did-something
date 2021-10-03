package com.choi.notice.entity;

import com.choi.notice.boot.config.BootConfiguration;
import com.choi.notice.boot.config.MongoDbConfiguration;
import com.choi.notice.sns.SnsType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {BootConfiguration.class, MongoDbConfiguration.class})
@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class InfluenceRepositoryTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InfluenceRepository influenceRepository;

	@Test
	public void saveTest() {
		Mono<Influence> test = this.influenceRepository.save(new Influence("test", SnsType.twitter));
		StepVerifier.create(test)
		            .assertNext(influence -> {
						assertThat(influence.getUserId()).isEqualTo("test");
						assertThat(influence.getSnsType()).isEqualTo(SnsType.twitter);
		            })
		            .verifyComplete();
	}
}
