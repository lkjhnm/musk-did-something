package com.choi.notice.entity;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluenceRepository extends ReactiveMongoRepository<Influence, String> {

}
