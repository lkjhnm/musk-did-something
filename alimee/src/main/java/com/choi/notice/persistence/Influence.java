package com.choi.notice.persistence;

import com.choi.notice.service.sns.SnsType;

public class Influence {

	private String id;
	private SnsType snsType;
	private SnsDetails<?> snsDetails;

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

	public Influence setSnsType(SnsType snsType) {
		this.snsType = snsType;
		return this;
	}

	public <T extends SnsDetails<?>> T getSnsDetail() {
		return (T) snsDetails.getDetail();
	}

	public Influence setSnsDetail(SnsDetails<? extends SnsDetails> snsDetailsDetail) {
		this.snsDetails = snsDetailsDetail;
		return this;
	}

	@Override
	public String toString() {
		return "Influence{" +
				"id='" + id + '\'' +
				", snsType=" + snsType +
				", snsDetails=" + snsDetails +
				'}';
	}
}
