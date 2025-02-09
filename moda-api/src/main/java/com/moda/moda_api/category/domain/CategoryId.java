package com.moda.moda_api.category.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moda.moda_api.category.exception.InvalidCategoryIdException;
import lombok.Value;

@Value
public class CategoryId {
    public static final Long ALL = 1L;
    Long value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public CategoryId(@JsonProperty("value") Long value) {
        validateCategoryId(value);
        this.value = value;
    }

    public static CategoryId all() {
        return new CategoryId(ALL);
    }

    /**
     * CategoryId 값 검증
     */
    private void validateCategoryId(Long value) {
        if (value == null) {
            throw new InvalidCategoryIdException("카테고리 ID가 존재하지 않습니다.");
        }
    }
}
