package com.moda.moda_api.category.application.mapper;

import com.moda.moda_api.category.application.response.CategoryListResponse;
import com.moda.moda_api.category.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryDtoMapper {

    public CategoryListResponse toResponse(Category category) {
        return CategoryListResponse.builder()
                .categoryId(category.getCategoryId().getValue())
                .category(category.getCategory())
                .position(category.getPosition().getValue())
                .build();
    }
}
