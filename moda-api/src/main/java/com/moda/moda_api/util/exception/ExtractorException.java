package com.moda.moda_api.util.exception;

import lombok.Getter;

@Getter
public class ExtractorException extends RuntimeException {
	public ExtractorException(String message, Throwable cause) {
		super(message, cause);
	}
}