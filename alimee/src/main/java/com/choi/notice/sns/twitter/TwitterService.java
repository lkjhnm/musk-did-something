package com.choi.notice.sns.twitter;

import com.choi.notice.boot.config.BootConfiguration;
import com.choi.notice.entity.Influence;
import com.choi.notice.sns.SnsService;
import com.choi.notice.sns.twitter.entity.TwitterUser;
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

@Service
public class TwitterService implements SnsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private final Sinks.Many<Influence> eventEmitter = Sinks.many().multicast().onBackpressureBuffer();
	private final Flux<Influence> influenceChecker = eventEmitter.asFlux();

	@Value("${twitter.api.validate.base.uri}")
	private String validateBaseUri;

	BootConfiguration.ApiConfig apiConfig;

	private String bearToken;

	@PostConstruct
	public void initialize() {
		this.bearToken = this.apiConfig.getProperty("twitter.api.bearer.token");
		initSubscribe();
	}

	private void initSubscribe() {
		influenceChecker
				.subscribe(val -> logger.info("this is influence Subscriber : {}", val));
	}

	@Autowired
	public void setApiConfig(BootConfiguration.ApiConfig apiConfig) {
		this.apiConfig = apiConfig;
	}

	//todo: 구독목록에 이미 포함된 경우 해당 인플루언서를 구독하는 사용자만 추가
	@Override
	public Mono<ResponseEntity<Void>> subscribeInfluence(Influence influence) {
		return Mono.from(validateInfluence(influence))
				.flatMap(this::publishSubscribeEvent)
				.log()
				.onErrorReturn(ResponseEntity.status(500).build());
	}

	private Mono<Influence> validateInfluence(Influence influence) {
		 WebClient webClient = WebClient
				.builder()
				.baseUrl(String.format(validateBaseUri, influence.getName()))
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

	private Mono<ResponseEntity<Void>> publishSubscribeEvent(Influence influence) {
		Sinks.EmitResult emitResult = eventEmitter.tryEmitNext(influence);
		return Mono.defer(() -> emitResult.isSuccess() ?
				Mono.just(ResponseEntity.status(200).build()) :
				Mono.error(new RuntimeException("publish emitter error is occurred"))
		);
	}
}
