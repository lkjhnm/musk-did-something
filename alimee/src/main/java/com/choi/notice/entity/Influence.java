package com.choi.notice.entity;

import com.choi.notice.sns.SnsType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "notice")
public class Influence {

	@Id
	private String id;
	private final String userId;
	private final SnsType snsType;

	public Influence(String userId, SnsType snsType) {
		this.userId = userId;
		this.snsType = snsType;
	}

	public String getUserId() {
		return userId;
	}

	public SnsType getSnsType() {
		return snsType;
	}

	@Override
	public String toString() {
		return "Influence{" +
				"id='" + userId + '\'' +
				", snsType=" + snsType +
				'}';
	}
}
