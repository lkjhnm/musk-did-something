package com.choi.notice.entity;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SubscribeRepository extends ReactiveMongoRepository<Subscribe, String> {

	Mono<Subscribe> findByInfluence(Influence influence);
}
