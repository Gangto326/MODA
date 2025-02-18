package com.moda.moda_api.card.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicateUrlException extends RuntimeException {
	private final String userId;

	public DuplicateUrlException(String message, String userId) {
		super(message);
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
}