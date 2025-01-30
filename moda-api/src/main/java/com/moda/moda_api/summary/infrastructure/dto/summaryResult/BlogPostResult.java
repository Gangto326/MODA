package com.moda.moda_api.summary.infrastructure.dto.summaryResult;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostResult {
	private OuterData data;  // 최상위 data 객체 추가
	private String status;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OuterData {
		private String type;
		private String resultLanguage;
		private BlogPostData data;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class BlogPostData {
		private List<BlogPostEntry> blogPost;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class BlogPostEntry {
		private String title;
		private String content;
	}
}