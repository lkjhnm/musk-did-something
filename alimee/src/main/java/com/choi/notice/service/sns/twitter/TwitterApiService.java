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

	public Mono<Subscribe> validateInfluence(Influence influence) {
		return buildWebClient(String.format(validateBaseUri, influence.getId()))
				.get()
				.exchangeToMono(clientResponse -> clientResponse
						.bodyToMono(TwitterUser.class)
						.flatMap(twitterUser -> twitterUser.isError() ?
								Mono.error(new RuntimeException("this is unknown user")) :
								getNewSubscribe(influence, twitterUser))
				);
	}

	public Mono<Tuple2<TwitterUser, Tweet>> checkTweet(TwitterUser twitterUser) {
		return getTweet(twitterUser)
				.map(tweet -> Tuples.of(twitterUser, tweet));
	}

	private Mono<Tweet> getTweet(TwitterUser twitterUser) {
		return buildWebClient(String.format(subscribeBaseUri, twitterUser.getData().getId()))
				.get()
				.exchangeToMono(clientResponse -> clientResponse.bodyToMono(Tweet.class));
	}

	private Mono<Subscribe> getNewSubscribe(Influence influence, TwitterUser twitterUser) {
		return getTweet(twitterUser)
				.map(tweet -> twitterUser.setTweet(tweet))
				.map(tw -> new Subscribe(Collections.emptyList(), influence.setSnsDetail(tw)));
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
