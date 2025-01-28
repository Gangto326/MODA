package com.moda.moda_api.summary.domain.model;

import java.time.LocalDateTime;

import lombok.Getter;


@Getter
public class CrawledContent {
	private final String title;
	private final String body;
	private final String url;
	private final LocalDateTime crawledAt;

	public CrawledContent(String url, String title, String body) {
		this.url = url;
		this.title = title;
		this.body = body;
		this.crawledAt = LocalDateTime.now();
	}

}