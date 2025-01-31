package com.moda.moda_api.user.exception;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String message) {
        super (message);
    }
}
