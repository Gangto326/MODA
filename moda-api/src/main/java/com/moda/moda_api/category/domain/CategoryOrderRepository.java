package com.moda.moda_api.category.domain;

import com.moda.moda_api.user.domain.UserId;

import java.util.List;

public interface CategoryOrderRepository {
    List<Category> findByUserId(UserId userIdObj);

    void saveAll(List<Category> categoryList);

    void save(Category category);
}
