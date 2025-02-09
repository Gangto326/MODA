package com.moda.moda_api.crawling.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtractedContent {
	private final String text;
	private final String[] images;

	public static ExtractedContent empty() {
		return ExtractedContent.builder()
			.text("")
			.images(new String[0])  // 빈 배열 반환
			.build();
	}
}
