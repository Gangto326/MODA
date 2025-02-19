package com.moda.moda_api.common.exception;

import lombok.Getter;

@Getter
public class ContentExtractionException extends RuntimeException {
	private final String userId;

	public ContentExtractionException(String message, String userId, Throwable cause) {
		super(message, cause);
		this.userId = userId;
	}
}