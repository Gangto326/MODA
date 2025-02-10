package com.moda.moda_api.card.infrastructure.external.mapper;

import com.moda.moda_api.card.domain.EmbeddingVector;
import org.springframework.stereotype.Component;

@Component
public class EmbeddingVectorMapper {
    public EmbeddingVector toDomain(float[] embeddingVector) {
        return new EmbeddingVector(embeddingVector);
    }
}
