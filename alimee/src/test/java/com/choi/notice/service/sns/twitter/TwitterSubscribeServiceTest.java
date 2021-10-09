package com.choi.notice.service.sns.twitter;

import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class TwitterSubscribeServiceTest extends AbstractTwitterServiceTest {

	@Value("${twitter.api.validate.base.uri}")
	String validateBaseUri;

	@Test
	public void validateInfluenceTest() {
		WebClient webClient = WebClient
				.builder()
				.baseUrl(String.format(validateBaseUri, "elonmusk"))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(this.twitterToken))
				.build();

		Mono<TwitterUser> twitterUserMono = executeValidate(webClient);
		StepVerifier
				.create(twitterUserMono)
				.assertNext(twitterUser -> assertThat(twitterUser))
				.verifyComplete();
	}

	private Mono<TwitterUser> executeValidate(WebClient webClient) {
		return webClient.get()
		                .exchangeToMono(clientResponse -> {
			                if (clientResponse.statusCode()
			                                  .isError()) {
				                return Mono.error(new RuntimeException("Response from server status code is error"));
			                }
			                return clientResponse
					                .bodyToMono(TwitterUser.class)
					                .log()
					                .flatMap(dto -> dto.isError() ?
							                Mono.error(new RuntimeException("this is unknown user")) : Mono.just(dto));
		                });
	}
}
