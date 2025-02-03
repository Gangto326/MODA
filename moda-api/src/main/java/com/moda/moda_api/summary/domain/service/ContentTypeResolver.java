package com.moda.moda_api.summary.domain.service;

import java.util.List;

import com.moda.moda_api.card.domain.ContentType;

public class ContentTypeResolver {

	private static final List<String> BLOG_SITES = List.of("tistory.com", "velog.io" ,"medium.com", "brunch.co.kr");
	private static final List<String> NEWS_SITES = List.of("naver.com", "daum.net", "bbc.com", "cnn.com");

	public static ContentType resolve(String url) {
		if (url.contains("youtube.com")) {
			return ContentType.YOUTUBE;
		}

		if (BLOG_SITES.stream().anyMatch(url::contains)) {
			return ContentType.BLOG;
		}

		if (NEWS_SITES.stream().anyMatch(url::contains)) {
			return ContentType.NEWS;
		}

		return ContentType.OTHERS;
	}
}