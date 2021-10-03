package com.choi.notice.service.subscribe.entity;

import com.choi.notice.persistence.Influence;

public class SubscribeUser {
	private Influence influence;
	private String userId;

	public Influence getInfluence() {
		return influence;
	}

	public void setInfluence(Influence influence) {
		this.influence = influence;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
