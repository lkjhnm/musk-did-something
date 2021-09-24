package com.choi.notice.sns;

import reactor.core.publisher.Mono;

public interface SnsService {

	void validateInfluence(String influenceId);

	Mono<Void> subscribeInfluence(String influenceId);
}
