package com.choi.notice.service.sns.twitter.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.annotation.Transient;


import java.util.List;

public class Tweet {

	// todo: 리스트 중 첫번째 데이터(가장 최근)만 바인딩하도록
	@Transient
	private List<Data> data;
	private Meta meta;

	public String getRecentlyTweetId() {
		return this.meta.newestId;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public static class Data {
		private String id;
		private String text;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return "Data{" +
					"id='" + id + '\'' +
					", text='" + text + '\'' +
					'}';
		}
	}

	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Meta {
		private String oldestId;
		private String newestId;

		public String getOldestId() {
			return oldestId;
		}

		public void setOldestId(String oldestId) {
			this.oldestId = oldestId;
		}

		public String getNewestId() {
			return newestId;
		}

		public void setNewestId(String newestId) {
			this.newestId = newestId;
		}

		@Override
		public String toString() {
			return "Meta{" +
					"oldestId='" + oldestId + '\'' +
					", newestId='" + newestId + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "Tweet{" +
				"meta=" + meta +
				'}';
	}
}
