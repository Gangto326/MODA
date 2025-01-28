package com.moda.moda_api.summary.domain.model;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class Content {
	private final String title;
	private final String body;
	private final LocalDateTime crawledAt;

	public Content(String title, String body) {
		this.title = title;
		this.body = body;
		this.crawledAt = LocalDateTime.now();
	}

	// getters...
}