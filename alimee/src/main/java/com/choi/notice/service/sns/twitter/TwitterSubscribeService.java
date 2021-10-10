package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.Subscribe;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.SnsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class TwitterSubscribeService implements SnsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private final Sinks.Many<Subscribe> eventEmitter = Sinks.many().multicast().onBackpressureBuffer();

	private SubscribeRepository subscribeRepository;
	private TwitterApiService twitterApiService;

	@Bean
	public Flux<Subscribe> subscribePublisher() {
		return eventEmitter.asFlux();
	}

	@Autowired
	public void setSubscribeRepository(SubscribeRepository subscribeRepository) {
		this.subscribeRepository = subscribeRepository;
	}

	@Autowired
	public void setTwitterApiService(TwitterApiService twitterApiService) {
		this.twitterApiService = twitterApiService;
	}

	@Override
	public Mono<ResponseEntity<Void>> subscribeInfluence(Influence influence, String userId) {
		return Mono.just(influence)
				.flatMap(twitterApiService::validateInfluence)
				.flatMap(subscribe -> this.saveSubscribe(subscribe, userId))
				.flatMap(this::publishSubscribeEvent)
				.log()  //todo: 필요시에만 로그를 출력하도록?
				.onErrorReturn(ResponseEntity.status(500).build());
	}

	private Mono<Subscribe> saveSubscribe(Subscribe subscribe, String userId) {
		subscribe.addUserId(userId);
		return this.subscribeRepository.save(subscribe);
	}



	private Mono<ResponseEntity<Void>> publishSubscribeEvent(Subscribe subscribe) {
		Sinks.EmitResult emitResult = eventEmitter.tryEmitNext(subscribe);
		return Mono.defer(() -> emitResult.isSuccess() ?
				Mono.just(ResponseEntity.status(200).build()) :
				Mono.error(new RuntimeException("publish emitter error is occurred"))
		);
	}
}
