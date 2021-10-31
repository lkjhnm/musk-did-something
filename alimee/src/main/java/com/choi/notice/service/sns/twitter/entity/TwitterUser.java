package com.choi.notice.service.sns.twitter.entity;

import com.choi.notice.persistence.SnsDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TwitterUser implements SnsDetails<TwitterUser> {

	List<Error> errors;
	@JsonProperty("data")
	Profile profile;
	Tweet tweet;

	@Override
	@JsonIgnore
	public TwitterUser getDetail() {
		return this;
	}

	public boolean isError() {
		return errors != null || (errors == null && profile == null);
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
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
				", profile=" + profile +
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

	public static class Profile {
		private String id;
		private String name;
		private String username;

		public String getId() {
			return id;
		}

		public Profile setId(String id) {
			this.id = id;
			return this;
		}

		public String getName() {
			return name;
		}

		public Profile setName(String name) {
			this.name = name;
			return this;
		}

		public String getUsername() {
			return username;
		}

		public Profile setUsername(String username) {
			this.username = username;
			return this;
		}

		@Override
		public String toString() {
			return "Profile{" +
					"id='" + id + '\'' +
					", name='" + name + '\'' +
					", username='" + username + '\'' +
					'}';
		}
	}
}
