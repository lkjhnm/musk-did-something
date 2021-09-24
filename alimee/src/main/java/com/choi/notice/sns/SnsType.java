package com.choi.notice.sns;

import com.choi.notice.sns.twitter.TwitterService;

public enum SnsType {
	TWITTER(TwitterService.class)
	;

	private Class<? extends SnsService> serviceClazz;

	SnsType(Class<? extends SnsService> serviceClazz) {
		this.serviceClazz = serviceClazz;
	}

	public Class<? extends SnsService> getClazz() {
		return this.serviceClazz;
	}
}
