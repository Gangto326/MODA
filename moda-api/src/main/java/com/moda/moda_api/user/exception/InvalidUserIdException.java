package com.moda.moda_api.user.exception;

public class InvalidUserIdException extends RuntimeException {
    public InvalidUserIdException(String message) {
        super (message);
    }
}
