package com.moda.moda_api.card.exception;

import org.springframework.beans.factory.annotation.Autowired;

import com.moda.moda_api.card.domain.Card;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicateCardException extends RuntimeException {
    private final String userId;

    public DuplicateCardException(String message, String userId) {
        super(message);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}