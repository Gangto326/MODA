package com.moda.moda_api.user.exception;

import jakarta.mail.MessagingException;

public class EmailException extends RuntimeException {
	public EmailException(String message) {
		super(message);
	}

	public EmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailException(Throwable cause) {
		super(cause);
	}
}