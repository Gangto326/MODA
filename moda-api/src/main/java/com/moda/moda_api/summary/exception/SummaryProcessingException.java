package com.moda.moda_api.summary.exception;


public class SummaryProcessingException extends RuntimeException {
	public SummaryProcessingException(String message) {
		super(message);
	}

	public SummaryProcessingException(String message, Throwable cause) {
		super(message, cause);
	}
}