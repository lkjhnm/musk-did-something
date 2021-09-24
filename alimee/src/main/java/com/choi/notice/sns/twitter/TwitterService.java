package com.choi.notice.sns.twitter;

import com.choi.notice.sns.SnsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TwitterService implements SnsService {

	@Override
	public void validateInfluence(String influenceId) {

	}

	@Override
	public Mono<Void> subscribeInfluence(String influenceId) {
		return null;
	}
}
