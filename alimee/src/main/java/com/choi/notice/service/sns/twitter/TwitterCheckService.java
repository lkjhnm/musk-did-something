package com.choi.notice.service.sns.twitter;

import com.choi.notice.persistence.Subscribe;
import com.choi.notice.service.sns.twitter.entity.Tweet;
import com.choi.notice.service.sns.twitter.entity.TwitterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Service
public class TwitterCheckService {

	private Flux<Subscribe> subscribePublisher;

	private TwitterApiService twitterApiService;

	@Autowired
	public void setTwitterApiService(TwitterApiService twitterApiService) {
		this.twitterApiService = twitterApiService;
	}

	@Autowired
	public void setSubscribePublisher(Flux<Subscribe> subscribePublisher) {
		this.subscribePublisher = subscribePublisher;
	}

	@PostConstruct
	public void initialize() {
		this.subscribePublisher
				.log()
				.map(subscribe -> subscribe.getInfluence().<TwitterUser>getSnsDetail())
				.flatMap(twitterUser -> this.twitterApiService.checkTweet(twitterUser))
//				.filter()
				.subscribe(tweet -> System.out.println(tweet));
	}


	private boolean checkTweet(Tweet tweet) {
		return false;
	}
}
