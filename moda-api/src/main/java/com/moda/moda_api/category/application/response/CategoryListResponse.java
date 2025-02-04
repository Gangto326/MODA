package com.moda.moda_api.category.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListResponse {
    private Long categoryId;
    private String category;
    private Integer position;
}
