package com.choi.notice.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.choi.notice.persistence"})
public class MongoDbConfiguration {

}
