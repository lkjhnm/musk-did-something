package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Subscribe;
import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.twitter.entity.Tweet;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.function.Supplier;

@Service
public class TwitterCheckService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SubscribeRepository subscribeRepository;

	private TwitterApiService twitterApiService;

	private Flux<Subscribe> tweetChecker;

	private Flux<Subscribe> tweetUpdater;

	private Sinks.Many<Subscribe> subscribeEmitter = Sinks.many().multicast().onBackpressureBuffer();

	@Value("${tweet.check.debug.enable}")
	boolean debugMode;

	@Autowired
	public void setTwitterApiService(TwitterApiService twitterApiService) {
		this.twitterApiService = twitterApiService;
	}

	@Autowired
	public void setSubscribeRepository(SubscribeRepository subscribeRepository) {
		this.subscribeRepository = subscribeRepository;
	}

	@Bean
	public Supplier<Flux<Subscribe>> notice() {
		return () -> this.subscribeEmitter.asFlux();
	}

	@PostConstruct
	public void initialize() {
		initTweetChecker();
		initTweetUpdater();
	}

	private void initTweetChecker() {
		tweetChecker = Flux.interval(debugMode ? Duration.ofSeconds(10) : Duration.ofMinutes(1))
		                   .flatMap(unused -> this.subscribeRepository.findAll())
		                   .flatMap(this::checkNewTweetPost)
		                   .onErrorContinue((throwable, o) -> logger.warn("Exception occurred during scheduled tweet Check, Element : {}", o));
		this.tweetChecker
				.subscribe(subscribe -> this.subscribeEmitter.tryEmitNext(subscribe));
	}

	private void initTweetUpdater() {
		this.tweetUpdater = this.subscribeEmitter.asFlux();
		this.tweetUpdater
				.flatMap(this.subscribeRepository::save)
				.onErrorContinue((throwable, o) -> logger.warn("Exception occurred during tweet update, Element : {}", o))
				.subscribe();
	}

	private Mono<Subscribe> checkNewTweetPost(Subscribe subscribe) {
		TwitterUser fetched = subscribe.getInfluence().getSnsDetail();
		return twitterApiService.getTweet(fetched)
		                        .map(tweet -> Tuples.of(fetched.getTweet(), tweet))
		                        .filter(this::compareTweet)
		                        .map(tuple -> tuple.getT2())
		                        .map(newest -> {
			                        subscribe.getInfluence().<TwitterUser>getSnsDetail().setTweet(newest);
			                        return subscribe;
		                        });
	}

	private boolean compareTweet(Tuple2<Tweet,Tweet> tuple) {
		return tuple.getT2().getData() != null && !tuple.getT1().getRecentlyTweetId().equals(tuple.getT2().getRecentlyTweetId());
	}
}
