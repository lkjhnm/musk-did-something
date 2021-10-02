package com.choi.notice.entity;

import com.choi.notice.boot.config.BootConfiguration;
import com.choi.notice.boot.config.MongoDbConfiguration;
import com.choi.notice.sns.SnsType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = {BootConfiguration.class, MongoDbConfiguration.class})
@EnableAutoConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
public class InfluenceRepositoryTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InfluenceRepository influenceRepository;

}
