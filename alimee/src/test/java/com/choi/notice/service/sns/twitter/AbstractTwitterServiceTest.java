package com.choi.notice.service.sns.twitter;

import com.choi.notice.boot.config.BootConfiguration;
import com.choi.notice.boot.config.MongoDbConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 *  테스트 코드 샘플!
 *  https://github.com/spring-projects/spring-data-mongodb/blob/main/spring-data-mongodb/src/test/java/org/springframework/data/mongodb/repository/ReactiveMongoRepositoryTests.java
*/
@SpringJUnitConfig(classes = {BootConfiguration.class, MongoDbConfiguration.class})
@EnableAutoConfiguration
@TestPropertySource(locations = {"classpath:application-test.properties", "classpath:/api-config.properties"})
public abstract class AbstractTwitterServiceTest {

	@Autowired
	protected Environment environment;
}
