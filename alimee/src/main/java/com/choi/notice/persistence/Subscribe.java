package com.choi.notice.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Subscribe {

	@Id
	private String id;
	/** 알림을 위한 임시 컬럼 추후 변경 가능 */
	private List<String> userId = new ArrayList<>();
	@Indexed(unique = true)
	private Influence influence;

	public Subscribe(List<String> userId, Influence influence) {
		this.userId.addAll(userId);
		this.influence = influence;
	}

	public List<String> getUserId() {
		return userId;
	}

	public Influence getInfluence() {
		return influence;
	}

	public Subscribe addUserMail(String userMail) {
		this.userId.add(userMail);
		return this;
	}

	@Override
	public String toString() {
		return "Subscribe{" +
				"id='" + id + '\'' +
				", userMail='" + userId + '\'' +
				", influence=" + influence +
				'}';
	}
}
