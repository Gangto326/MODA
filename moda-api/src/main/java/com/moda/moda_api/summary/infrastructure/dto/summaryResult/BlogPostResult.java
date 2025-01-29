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
	private String type = "blogPost";
	private String resultLanguage;
	private BlogPostData data;
	private String status;  // 추가된 부분

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