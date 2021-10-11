package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.SubscribeRepository;
import com.choi.notice.service.sns.twitter.entity.Tweet;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class TwitterCheckService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SubscribeRepository subscribeRepository;

	private TwitterApiService twitterApiService;

	private Flux<Tuple2<Tweet, Tweet>> tweetChecker;

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

	@PostConstruct
	public void initialize() {
		initTweetChecker();
	}

	private void initTweetChecker() {
		tweetChecker = Flux.interval(debugMode ? Duration.ofSeconds(10) : Duration.ofMinutes(1))
		                   .flatMap(unused -> this.subscribeRepository.findAll())
		                   .map(subscribe -> subscribe.getInfluence().<TwitterUser>getSnsDetail())
		                   .flatMap(this::zipFetchedTweetAndRecentlyTweet)
		                   .filter(this::checkPostNewTweet);
		this.tweetChecker
				.subscribe(tuple -> logger.info("Recently Tweet Update Detected! oldest : [{}], latest : [{}]"
						, tuple.getT1(), tuple.getT2()));
	}

	private Mono<Tuple2<Tweet, Tweet>> zipFetchedTweetAndRecentlyTweet(TwitterUser twitterUser) {
		return twitterApiService.getTweet(twitterUser)
		                        .map(tweet -> Tuples.of(twitterUser.getTweet(), tweet));
	}


	private boolean checkPostNewTweet(Tuple2<Tweet,Tweet> tuple) {
		Tweet oldestTweet = tuple.getT1();
		Tweet recentlyTweet = tuple.getT2();
		return !oldestTweet.getRecentlyTweetId().equals(recentlyTweet.getRecentlyTweetId());
	}
}
