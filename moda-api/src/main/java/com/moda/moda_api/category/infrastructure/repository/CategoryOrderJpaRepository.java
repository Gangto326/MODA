package com.moda.moda_api.category.infrastructure.repository;

import com.moda.moda_api.category.infrastructure.entity.CategoryOrderEntiy;
import com.moda.moda_api.category.infrastructure.entity.CategoryOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryOrderJpaRepository extends JpaRepository<CategoryOrderEntiy, CategoryOrderId> {
    List<CategoryOrderEntiy> findByUserId(String value);
}
