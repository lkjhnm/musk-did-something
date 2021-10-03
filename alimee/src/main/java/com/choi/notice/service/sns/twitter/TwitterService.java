package com.choi.notice.service.sns.twitter;

import com.choi.notice.boot.config.BootConfiguration;
import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.Subscribe;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.SnsService;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class TwitterService implements SnsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private final Sinks.Many<Subscribe> eventEmitter = Sinks.many().multicast().onBackpressureBuffer();
	private final Flux<Subscribe> subscribeChecker = eventEmitter.asFlux();

	@Value("${twitter.api.validate.base.uri}")
	private String validateBaseUri;

	private SubscribeRepository subscribeRepository;

	BootConfiguration.ApiConfig apiConfig;

	private String bearToken;

	@PostConstruct
	public void initialize() {
		this.bearToken = this.apiConfig.getProperty("twitter.api.bearer.token");
		initSubscribe();
	}

	// todo: 트위터 스캔 서비스 기능 구현
	private void initSubscribe() {
		subscribeChecker
				.subscribe(val -> logger.info("this is influence Subscriber : {}", val));
	}

	@Autowired
	public void setApiConfig(BootConfiguration.ApiConfig apiConfig) {
		this.apiConfig = apiConfig;
	}

	@Autowired
	public void setSubscribeRepository(SubscribeRepository subscribeRepository) {
		this.subscribeRepository = subscribeRepository;
	}

	@Override
	public Mono<ResponseEntity<Void>> subscribeInfluence(Influence influence, String userId) {
		return Mono.from(validateInfluence(influence))
				.flatMap(this::findSubscribeByInfluence)
				.flatMap(subscribe -> this.saveSubscribe(subscribe, userId))
				.flatMap(this::publishSubscribeEvent)
				.log()  //todo: 필요시에만 로그를 출력하도록?
				.onErrorReturn(ResponseEntity.status(500).build());
	}

	private Mono<Subscribe> findSubscribeByInfluence(Influence influence) {
		return this.subscribeRepository.findByInfluence(influence)
		                               .defaultIfEmpty(new Subscribe(Collections.emptyList(), influence));
	}

	private Mono<Subscribe> saveSubscribe(Subscribe subscribe, String userId) {
		subscribe.addUserMail(userId);
		return this.subscribeRepository.save(subscribe);
	}

	private Mono<Influence> validateInfluence(Influence influence) {
		 WebClient webClient = WebClient
				.builder()
				.baseUrl(String.format(validateBaseUri, influence.getId()))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			    .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(bearToken))
				.build();

		 return webClient.get()
				 .exchangeToMono(clientResponse -> {
					 if (clientResponse.statusCode().isError()) {
						return Mono.error(new RuntimeException("Response from server status code is error"));
					 }
					 return clientResponse
							 .bodyToMono(TwitterUser.class)
							 .log()
							 .flatMap(dto -> dto.isError() ?
									 Mono.error(new RuntimeException("this is unknown user")) : Mono.just(influence));
				 });
	}

	private Mono<ResponseEntity<Void>> publishSubscribeEvent(Subscribe subscribe) {
		Sinks.EmitResult emitResult = eventEmitter.tryEmitNext(subscribe);
		return Mono.defer(() -> emitResult.isSuccess() ?
				Mono.just(ResponseEntity.status(200).build()) :
				Mono.error(new RuntimeException("publish emitter error is occurred"))
		);
	}
}
