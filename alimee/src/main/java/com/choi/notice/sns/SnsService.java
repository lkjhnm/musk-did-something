package com.choi.notice.sns;

import com.choi.notice.entity.Influence;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface SnsService {

	Mono<ResponseEntity<Void>> subscribeInfluence(Influence influence);
}
