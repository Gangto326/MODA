package com.moda.moda_api.common.exception;

import lombok.Getter;

@Getter
public class UnprocessableContentException extends RuntimeException {
	private final String userId;

	public UnprocessableContentException(String userId, String message) {
		super(message);
		this.userId = userId;
	}
}