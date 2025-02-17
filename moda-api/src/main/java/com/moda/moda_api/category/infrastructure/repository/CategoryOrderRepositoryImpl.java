package com.moda.moda_api.category.infrastructure.repository;

import com.moda.moda_api.category.domain.Category;
import com.moda.moda_api.category.domain.CategoryOrderRepository;
import com.moda.moda_api.category.infrastructure.entity.CategoryOrderEntiy;
import com.moda.moda_api.category.infrastructure.mapper.CategoryEntityMapper;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryOrderRepositoryImpl implements CategoryOrderRepository {
    private final CategoryOrderJpaRepository categoryOrderJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public List<Category> findByUserId(UserId userIdObj) {
        return categoryOrderJpaRepository.findByUserId(userIdObj.getValue()).stream()
                .map(categoryEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Category> categoryList) {
        categoryOrderJpaRepository.saveAll(categoryList.stream()
                .map(categoryEntityMapper::toEntity)
                .collect(Collectors.toList()));
    }

    @Override
    public void save(Category category) {
        categoryOrderJpaRepository.save(categoryEntityMapper.toEntity(category));
    }

    @Override
    public void saveAllEntities(List<CategoryOrderEntiy> entities) {
        categoryOrderJpaRepository.saveAll(entities);
    }

}
