package com.moda.moda_api.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super (message);
    }
}
