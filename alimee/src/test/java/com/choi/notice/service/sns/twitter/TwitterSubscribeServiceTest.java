package com.choi.notice.service.sns.twitter;

import com.choi.notice.boot.config.BootConfiguration;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static org.assertj.core.api.Assertions.*;

import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application-test.properties", "classpath:/api-config.properties"})
public class TwitterSubscribeServiceTest {

	@Autowired
	private Environment environment;

	@Value("${twitter.api.validate.base.uri}")
	String validateBaseUri;

	@Test
	public void validateInfluenceTest() {
		WebClient webClient = WebClient
				.builder()
				.baseUrl(String.format(validateBaseUri, "test"))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(environment.getProperty("twitter.api.bearer.token")))
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
