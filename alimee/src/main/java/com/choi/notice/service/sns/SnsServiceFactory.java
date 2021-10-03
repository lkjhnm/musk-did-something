package com.choi.notice.service.sns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SnsServiceFactory {

	private ApplicationContext context;

	@Autowired
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public SnsService create(SnsType snsType) {
		return this.context.getBean(snsType.getClazz());
	}
}
