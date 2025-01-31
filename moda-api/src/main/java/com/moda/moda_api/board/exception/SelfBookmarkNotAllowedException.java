package com.moda.moda_api.board.exception;

public class SelfBookmarkNotAllowedException extends RuntimeException {
    public SelfBookmarkNotAllowedException(String message) {
        super(message);
    }
}
