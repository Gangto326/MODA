package com.moda.moda_api.board.exception;

public class InvalidTitleException extends RuntimeException {
    public InvalidTitleException(String massage) {
        super(massage);
    }
}
