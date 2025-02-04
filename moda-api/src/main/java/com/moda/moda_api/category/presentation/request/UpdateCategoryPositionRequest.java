package com.moda.moda_api.category.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryPositionRequest {
    Long categoryId;
    Integer sourcePosition;
    Integer targetPosition;
}
