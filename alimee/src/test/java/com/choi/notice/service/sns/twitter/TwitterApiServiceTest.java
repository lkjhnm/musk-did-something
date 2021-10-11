package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.service.sns.SnsType;
import com.choi.notice.service.sns.twitter.entity.Tweet;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.annotation.PostConstruct;

//todo: API 테스트에서는 JPA를 사용하지 않는데 부모클래스에서 스캔하는 MongoDbConfiguration을 제외해야 할 것인가?
public class TwitterApiServiceTest extends AbstractTwitterServiceTest {

	protected String twitterToken;

	@Value("${twitter.api.single-tweet.base.uri}")
	private String subscribeBaseUri;

	@Value("${twitter.api.validate.base.uri}")
	String validateBaseUri;

	@PostConstruct
	public void initialize() {
		this.twitterToken = environment.getProperty("twitter.api.bearer.token");
	}

	@Test
	public void validateInfluenceTest() {
		Mono<TwitterUser> twitterUserMono = executeValidateApi().log();
		StepVerifier
				.create(twitterUserMono)
				.assertNext(subscribe -> Assertions.assertNotNull(subscribe))
				.verifyComplete();
	}

	//https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets#Default
	//44196397
	@Test
	public void tweetCheckApiTest() {
		Mono<Tweet> mapMono = executeTweetCheckApi().log();
		StepVerifier
				.create(mapMono)
				.assertNext(value -> Assertions.assertNotNull(value))
				.verifyComplete();
	}

	private Mono<TwitterUser> executeValidateApi() {
		Influence elonmusk = new Influence("elonmusk", SnsType.twitter);
		WebClient webClient = WebClient
				.builder()
				.baseUrl(String.format(validateBaseUri, elonmusk.getId()))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(this.twitterToken))
				.build();
		return webClient.get()
		                .exchangeToMono(clientResponse -> clientResponse
				                .bodyToMono(TwitterUser.class)
		                );
//				                .flatMap(dto -> dto.isError() ?
//						                Mono.error(new RuntimeException("this is unknown user")) :
//						                Mono.just(new Subscribe(Collections.emptyList(), elonmusk.setSnsDetail(dto))))
//		                );
	}

	private Mono<Tweet> executeTweetCheckApi() {
		WebClient webClient = WebClient
				.builder()
				.baseUrl(String.format(subscribeBaseUri, "44196397"))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(this.twitterToken))
				.build();

		return webClient.get()
		                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Tweet.class));
	}
}
