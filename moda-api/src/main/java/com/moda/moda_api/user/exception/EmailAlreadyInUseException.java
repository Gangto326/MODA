package com.moda.moda_api.user.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String message) {
        super (message);
    }
}
