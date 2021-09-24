package com.choi.notice.subscribe;

import com.choi.notice.sns.SnsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller(value = "subscribe")
public class SubscribeController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SubscribeService subscribeService;

	@Autowired
	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{influence-id}/{sns-type}")
	public Mono<Void> subscribeInfluence(@PathVariable("influence-id") String influenceId,
	                                      @PathVariable("sns-type") SnsType snsType) {
		logger.debug("influence-id : {} , sns-type : {}", influenceId, snsType);
		return this.subscribeService.subscribe(influenceId, snsType);
	}
}
