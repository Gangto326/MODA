package com.moda.moda_api.card.domain;

import java.util.Random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moda.moda_api.card.exception.InvalidVectorException;
import lombok.Value;

@Value
public class EmbeddingVector {
    public static final int DIMENSION = 768;
    private final float[] values;


    // Test용 기본 생성자. 나중에 지워야 합니다.
    public EmbeddingVector() {
        this.values = new float[DIMENSION];
        Random random = new Random();
        for (int i = 0; i < DIMENSION; i++) {
            this.values[i] = random.nextFloat(); // 0.0 ~ 1.0 사이의 랜덤값
        }
    }

    @JsonCreator
    public EmbeddingVector(@JsonProperty("values") float[] values) {
        validateVector(values);
        this.values = values.clone();
    }

    public float[] getValues() {
        return values.clone();
    }

    private void validateVector(float[] values) {
        if (values == null) {
            throw new InvalidVectorException("벡터 배열은 null일 수 없습니다.");
        }
        if (values.length != DIMENSION) {
            throw new InvalidVectorException("백터 배열의 길이는 " + DIMENSION + "이어야 합니다.");
        }
    }
}
