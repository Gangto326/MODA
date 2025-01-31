package com.moda.moda_api.user.exception;

public class NicknameAlreadyInUseException extends RuntimeException {
    public NicknameAlreadyInUseException(String message) {
        super (message);
    }
}
