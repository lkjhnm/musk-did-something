package com.choi.notice.entity;

import com.choi.notice.sns.SnsType;

public class Influence {

	private final String id;
	private final SnsType snsType;

	public Influence(String id, SnsType snsType) {
		this.id = id;
		this.snsType = snsType;
	}

	public String getId() {
		return id;
	}

	public SnsType getSnsType() {
		return snsType;
	}

	@Override
	public String toString() {
		return "Influence{" +
				"id='" + id + '\'' +
				", snsType=" + snsType +
				'}';
	}
}
