package com.choi.notice.service.sns;

import com.choi.notice.service.sns.twitter.TwitterService;

public enum SnsType {
	twitter(TwitterService.class)
	;

	private Class<? extends SnsService> serviceClazz;

	SnsType(Class<? extends SnsService> serviceClazz) {
		this.serviceClazz = serviceClazz;
	}

	public Class<? extends SnsService> getClazz() {
		return this.serviceClazz;
	}
}
