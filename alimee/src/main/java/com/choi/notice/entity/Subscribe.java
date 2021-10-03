package com.choi.notice.entity;

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
	private List<String> userMail = new ArrayList<>();
	@Indexed(unique = true)
	private Influence influence;

	public Subscribe(List<String> userMail, Influence influence) {
		this.userMail.addAll(userMail);
		this.influence = influence;
	}

	public List<String> getUserMail() {
		return userMail;
	}

	public Influence getInfluence() {
		return influence;
	}

	public Subscribe addUserMail(String userMail) {
		this.userMail.add(userMail);
		return this;
	}

	@Override
	public String toString() {
		return "Subscribe{" +
				"id='" + id + '\'' +
				", userMail='" + userMail + '\'' +
				", influence=" + influence +
				'}';
	}
}
