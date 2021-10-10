package com.choi.notice.service.sns.twitter.entity;

import com.choi.notice.persistence.SnsDetails;

import java.util.List;

public class TwitterUser implements SnsDetails<TwitterUser> {

	List<Error> errors;
	Data data;
	Tweet tweet;

	@Override
	public TwitterUser getDetail() {
		return this;
	}

	public boolean isError() {
		return errors != null || (errors == null && data == null);
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Tweet getTweet() {
		return tweet;
	}

	public TwitterUser setTweet(Tweet tweet) {
		this.tweet = tweet;
		return this;
	}

	@Override
	public String toString() {
		return "TwitterUser{" +
				"errors=" + errors +
				", data=" + data +
				", tweet=" + tweet +
				'}';
	}

	public static class Error {
		private String detail;
		private String title;
		private String resourceType;
		private String parameter;
		private String value;
		private String type;

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getResourceType() {
			return resourceType;
		}

		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}

		public String getParameter() {
			return parameter;
		}

		public void setParameter(String parameter) {
			this.parameter = parameter;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return "Error{" +
					"detail='" + detail + '\'' +
					", title='" + title + '\'' +
					", resourceType='" + resourceType + '\'' +
					", parameter='" + parameter + '\'' +
					", value='" + value + '\'' +
					", type='" + type + '\'' +
					'}';
		}
	}

	public static class Data {
		private String id;
		private String name;
		private String username;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		@Override
		public String toString() {
			return "Data{" +
					"id='" + id + '\'' +
					", name='" + name + '\'' +
					", username='" + username + '\'' +
					'}';
		}
	}
}
