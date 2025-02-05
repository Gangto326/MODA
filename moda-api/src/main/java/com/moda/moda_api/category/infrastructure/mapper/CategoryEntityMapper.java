package com.moda.moda_api.category.infrastructure.mapper;

import com.moda.moda_api.category.domain.Category;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.category.domain.Position;
import com.moda.moda_api.category.infrastructure.entity.CategoryOrderEntiy;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityMapper {

    public Category toDomain(CategoryOrderEntiy entity) {
        return Category.builder()
                .categoryId(new CategoryId(entity.getCategoryId()))
                .userId(new UserId(entity.getUserId()))
                .position(new Position(entity.getPosition()))
                .category(entity.getCategoryEntity().getCategory())
                .build();
    }

    public CategoryOrderEntiy toEntity(Category category) {
        return CategoryOrderEntiy.builder()
                .categoryId(category.getCategoryId().getValue())
                .userId(category.getUserId().getValue())
                .position(category.getPosition().getValue())
                .build();
    }
}
