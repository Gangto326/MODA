package com.moda.moda_api.crawling.domain.service;

import java.util.List;

import com.moda.moda_api.card.domain.CardContentType;

public class CardContentTypeResolver {

	private static final List<String> BLOG_SITES = List.of("tistory.com", "velog.io" ,"medium.com", "brunch.co.kr");
	private static final List<String> NEWS_SITES = List.of("naver.com", "daum.net", "bbc.com", "cnn.com");

	public static CardContentType resolve(String url) {
		if (url.contains("youtube.com")) {
			return CardContentType.VIDEO;
		}

		if (BLOG_SITES.stream().anyMatch(url::contains)) {
			return CardContentType.BLOG;
		}

		if (NEWS_SITES.stream().anyMatch(url::contains)) {
			return CardContentType.NEWS;
		}

		return CardContentType.OTHERS;
	}
}