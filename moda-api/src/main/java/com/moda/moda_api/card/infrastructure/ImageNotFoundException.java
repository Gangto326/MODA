package com.moda.moda_api.card.infrastructure;

public class ImageNotFoundException extends RuntimeException {
	public ImageNotFoundException(String message) {
		super(message);
	}
}
