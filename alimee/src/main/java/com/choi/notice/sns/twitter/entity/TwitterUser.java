package com.choi.notice.sns.twitter.entity;

import java.util.List;

public class TwitterUser {

	List<Error> errors;
	Data data;

	public boolean isError() {
		return errors != null;
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

	@Override
	public String toString() {
		return "TwitterUser{" +
				"errors=" + errors +
				", data=" + data +
				'}';
	}

	private static class Error {
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

	private static class Data {
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
