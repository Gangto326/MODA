package com.moda.moda_api.summary.infrastructure.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryResult<T> {
	private DataWrapper<T> data;
	private String status;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class DataWrapper<T> {
		private String type;
		private String resultLanguage;
		private T data;
	}

	// 각 타입별 내부 데이터 구조
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class BlogPostType {
		private List<BlogPost> blogPost;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class BlogPost {
			private String title;
			private String content;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class SummaryNoteType {
		private List<SummaryNote> summaryNote;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class SummaryNote {
			private double timestamp;
			private String title;
			private List<String> content;
			private String originalScript;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class RawScriptType {
		private String rawScript;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ShortSummaryType {
		private String summary;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class TimestampType {
		private List<Timestamp> result;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		@Builder
		public static class Timestamp {
			private String title;
			private double timestamp;
		}
	}
}