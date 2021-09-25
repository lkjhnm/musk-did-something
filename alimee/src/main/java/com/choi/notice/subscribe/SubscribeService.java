package com.choi.notice.subscribe;

import com.choi.notice.entity.Influence;
import com.choi.notice.sns.SnsService;
import com.choi.notice.sns.SnsServiceFactory;
import com.choi.notice.sns.SnsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class SubscribeService {

	private SnsServiceFactory snsServiceFactory;

	@Autowired
	public void setSnsServiceFactory(SnsServiceFactory snsServiceFactory) {
		this.snsServiceFactory = snsServiceFactory;
	}

	public Mono<ResponseEntity<Void>> subscribe(String influenceId, SnsType snsType) {
		SnsService snsService = this.snsServiceFactory.create(snsType);
		return snsService.subscribeInfluence(new Influence(influenceId, snsType));
	}

}
