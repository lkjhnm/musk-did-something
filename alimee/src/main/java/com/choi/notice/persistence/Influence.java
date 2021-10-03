package com.choi.notice.persistence;

import com.choi.notice.service.sns.SnsType;

public class Influence {

	private String id;
	private SnsType snsType;

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

	public void setId(String id) {
		this.id = id;
	}

	public void setSnsType(SnsType snsType) {
		this.snsType = snsType;
	}

	@Override
	public String toString() {
		return "Influence{" +
				"id='" + id + '\'' +
				", snsType=" + snsType +
				'}';
	}
}
