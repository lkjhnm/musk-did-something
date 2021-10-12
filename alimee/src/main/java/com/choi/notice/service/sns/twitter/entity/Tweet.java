package com.choi.notice.service.sns.twitter.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.List;

public class Tweet {

	@JsonDeserialize(using = FirstElementBindDeserializer.class)
	private Data data;

	private Meta meta;

	public String getRecentlyTweetId() {
		return this.meta.newestId;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
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
				"data=" + data +
				", meta=" + meta +
				'}';
	}

	private static class FirstElementBindDeserializer extends StdDeserializer<Data> {

		public FirstElementBindDeserializer() {
			this(null);
		}

		public FirstElementBindDeserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public Data deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
			List<Data> dataList = jsonParser.readValueAs(new TypeReference<List<Data>>() {});
			return dataList.get(0);
		}
	}
}
