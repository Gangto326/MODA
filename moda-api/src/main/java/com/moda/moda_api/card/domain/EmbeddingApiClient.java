package com.moda.moda_api.card.domain;

public interface EmbeddingApiClient {
    EmbeddingVector embedContent(String content);
}
