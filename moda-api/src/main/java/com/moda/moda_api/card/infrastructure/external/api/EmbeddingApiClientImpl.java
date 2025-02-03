package com.moda.moda_api.card.infrastructure.external.api;

import com.moda.moda_api.card.domain.EmbeddingApiClient;
import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.card.infrastructure.external.mapper.EmbeddingVectorMapper;
import com.moda.moda_api.card.infrastructure.external.request.ContentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmbeddingApiClientImpl implements EmbeddingApiClient {
    private final EmbeddingVectorMapper embeddingVectorMapper;
    private final WebClient embeddingWebClient;

    public EmbeddingVector embedContent(String content) {
        float[] embedding = embeddingWebClient.post()
                .uri("/embedding")
                .bodyValue(new ContentRequest(content))
                .retrieve()
                .bodyToMono(float[].class)
                .block();
        return embeddingVectorMapper.toDomain(embedding);
    }
}
