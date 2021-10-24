package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.Subscribe;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.SnsService;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TwitterSubscribeService implements SnsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SubscribeRepository subscribeRepository;
	private TwitterApiService twitterApiService;

	@Autowired
	public void setSubscribeRepository(SubscribeRepository subscribeRepository) {
		this.subscribeRepository = subscribeRepository;
	}

	@Autowired
	public void setTwitterApiService(TwitterApiService twitterApiService) {
		this.twitterApiService = twitterApiService;
	}

	@Override
	public Mono<ResponseEntity<Void>> subscribeInfluence(Influence influence) {
		return Mono.just(influence)
				.flatMap(this::getSubscribeOrElseGetNewOne)
				.flatMap(this.subscribeRepository::save)
				.map(unused -> ResponseEntity.status(200).<Void>build())
				.onErrorReturn(ResponseEntity.status(500).build());
	}

	private Mono<Subscribe> getSubscribeOrElseGetNewOne(Influence influence) {
		return this.subscribeRepository.findByInfluenceId(influence.getId())
		                               .switchIfEmpty(validateAndGetSubscribe(influence));
	}

	private Mono<Subscribe> validateAndGetSubscribe(Influence influence) {
		return twitterApiService.validateInfluence(influence)
		                        .flatMap(twitterUser -> twitterUser.isError() ?
				                        Mono.error(new RuntimeException("this is unknown user")) :
				                        createNewSubscribe(twitterUser, influence));
	}

	private Mono<Subscribe> createNewSubscribe(TwitterUser twitterUser, Influence influence) {
		return twitterApiService.getTweet(twitterUser)
		                        .map(tweet -> twitterUser.setTweet(tweet))
		                        .map(twu -> new Subscribe(influence.setSnsDetail(twu)));
	}
}
