package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Influence;
import com.choi.notice.persistence.Subscribe;
import com.choi.notice.service.sns.twitter.entity.Tweet;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collections;

@Service
public class TwitterApiService {

	@Value("${twitter.api.validate.base.uri}")
	private String validateBaseUri;

	@Value("${twitter.api.single-tweet.base.uri}")
	private String subscribeBaseUri;

	private TwitterConfigService twitterConfigService;

	@Autowired
	private void setTwitterConfigService(TwitterConfigService twitterConfigService) {
		this.twitterConfigService = twitterConfigService;
	}

	public Mono<TwitterUser> validateInfluence(Influence influence) {
		return buildWebClient(String.format(validateBaseUri, influence.getId()))
				.get()
				.exchangeToMono(clientResponse -> clientResponse.bodyToMono(TwitterUser.class));
	}

	public Mono<Tweet> getTweet(TwitterUser twitterUser) {
		return buildWebClient(String.format(subscribeBaseUri, twitterUser.getData().getId()))
				.get()
				.exchangeToMono(clientResponse -> clientResponse.bodyToMono(Tweet.class));
	}

	private WebClient buildWebClient(String baseUri) {
		return WebClient
				.builder()
				.baseUrl(baseUri)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(this.twitterConfigService.getTwitterToken()))
				.build();
	}
}
