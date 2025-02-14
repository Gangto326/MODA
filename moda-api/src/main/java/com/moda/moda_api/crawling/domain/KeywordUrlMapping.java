package com.moda.moda_api.crawling.domain;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordUrlMapping  {
	private String keyword;
	@Builder.Default
	private List<String> urls = new ArrayList<>();

	public void addUrl(String url) {
		validateUrl(url);
		if (!urls.contains(url)) {
			urls.add(url);
		}
	}

	private void validateUrl(String url) {
		if (url == null || url.trim().isEmpty()) {
			throw new IllegalArgumentException("URL cannot be null or empty");
		}
		try {
			new URI(url).toURL();
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid URL format");
		}
	}
}