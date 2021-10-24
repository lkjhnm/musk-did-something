package com.choi.notice.service.sns;

import com.choi.notice.persistence.Influence;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface SnsService {

	Mono<ResponseEntity<Void>> subscribeInfluence(Influence influence);
}
