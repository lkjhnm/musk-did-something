package com.choi.notice.entity;

import com.choi.notice.sns.SnsType;

public class Influence {

	private final String name;
	private final SnsType snsType;

	public Influence(String name, SnsType snsType) {
		this.name = name;
		this.snsType = snsType;
	}

	public String getName() {
		return name;
	}

	public SnsType getSnsType() {
		return snsType;
	}

	@Override
	public String toString() {
		return "Influence{" +
				"id='" + name + '\'' +
				", snsType=" + snsType +
				'}';
	}
}
