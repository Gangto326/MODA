package com.moda.moda_api.summary.domain.model;

import lombok.Getter;

@Getter
public class Summary {
	private String content;
	private String type;
	private String status;

	public Summary(String content) {
		this.content = content;
	}

	public Summary(String content, String type, String status) {
		this.content = content;
		this.type = type;
		this.status = status;
	}
}
