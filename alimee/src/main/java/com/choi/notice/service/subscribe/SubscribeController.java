package com.choi.notice.service.subscribe;

import com.choi.notice.persistence.Influence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SubscribeController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SubscribeService subscribeService;

	@Autowired
	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/subscribe")
	public Mono<ResponseEntity<Void>> subscribeInfluence(@RequestBody Influence influence) {
		logger.debug("Requested subscribe target Influence : {}", influence);
		return this.subscribeService.subscribe(influence);
	}
}
