package com.moda.moda_api.common.exception;

import lombok.Getter;

@Getter
public class ServerBusyException extends RuntimeException {
	private final String userId;

	public ServerBusyException(String message, String userId) {
		super(message);
		this.userId = userId;
	}
}
