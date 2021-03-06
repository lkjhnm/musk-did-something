package com.choi.notice.service.subscribe;

import com.choi.notice.persistence.Influence;
import com.choi.notice.service.sns.SnsService;
import com.choi.notice.service.sns.SnsServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SubscribeService {

	private SnsServiceFactory snsServiceFactory;

	@Autowired
	public void setSnsServiceFactory(SnsServiceFactory snsServiceFactory) {
		this.snsServiceFactory = snsServiceFactory;
	}

	public Mono<ResponseEntity<Void>> subscribe(Influence influence) {
		SnsService snsService = this.snsServiceFactory.create(influence.getSnsType());
		return snsService.subscribeInfluence(influence);
	}

}
