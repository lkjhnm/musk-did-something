package com.choi.notice.service.sns.twitter;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application-test.properties", "classpath:/api-config.properties"})
public abstract class AbstractTwitterServiceTest {

	@Autowired
	protected Environment environment;

	protected String twitterToken;

	@PostConstruct
	public void initialize() {
		this.twitterToken = environment.getProperty("twitter.api.bearer.token");
	}
}
