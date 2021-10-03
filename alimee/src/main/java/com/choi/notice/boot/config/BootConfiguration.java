package com.choi.notice.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.stereotype.Component;

/**
 * 서비스의 Spring 설정을 관리하는 클래스
 */
@Configuration
@ComponentScan(basePackages = {"com.choi.notice.service"})
public class BootConfiguration {

	@Component
	@PropertySource("classpath:/api-config.properties")
	public static class ApiConfig {
		private Environment environment;

		@Autowired
		public ApiConfig(Environment environment) {
			this.environment = environment;
		}

		public String getProperty(String key) {
			return this.environment.getProperty(key);
		}
	}
}
