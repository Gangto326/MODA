package com.moda.moda_api.card.domain;

public interface CardViewCountRepository {
    void incrementViewCount(CardId cardId);
}
