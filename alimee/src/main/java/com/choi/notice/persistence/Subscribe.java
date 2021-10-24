package com.choi.notice.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Subscribe {

	@Id
	private String id;
	@Indexed(unique = true)
	private Influence influence;

	public Subscribe(Influence influence) {
		this.influence = influence;
	}

	public Influence getInfluence() {
		return influence;
	}

	@Override
	public String toString() {
		return "Subscribe{" +
				"id='" + id + '\'' +
				", influence=" + influence +
				'}';
	}
}
