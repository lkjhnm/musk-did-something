package com.choi.notice.service.sns.twitter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

public class TwitterCheckServiceTest extends AbstractTwitterServiceTest {

	@Value("${twitter.api.single-tweet.base.uri}")
	private String subscribeBaseUri;

	//https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets#Default
	//44196397
	@Test
	public void tweetCheckApiTest() {
		Mono<Map> mapMono = executeTweetCheckApi();
		StepVerifier
				.create(mapMono)
				.assertNext(value -> Assertions.assertThat(value))
				.verifyComplete();
	}

	@Test
	public void recentlyTweetDetectTest() {

	}

	private Mono<Map> executeTweetCheckApi() {
		WebClient webClient = WebClient
				.builder()
				.baseUrl(String.format(subscribeBaseUri, "44196397"))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(this.twitterToken))
				.build();

		Mono<Map> mapMono = webClient.get()
		                             .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Map.class)
		                                                                             .log());
		return mapMono;
	}
}
