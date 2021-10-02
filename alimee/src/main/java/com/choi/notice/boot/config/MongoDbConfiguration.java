package com.choi.notice.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.choi.notice.entity")
public class MongoDbConfiguration {

}
