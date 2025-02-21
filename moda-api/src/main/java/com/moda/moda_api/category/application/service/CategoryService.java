package com.moda.moda_api.category.application.service;

import com.moda.moda_api.category.application.mapper.CategoryDtoMapper;
import com.moda.moda_api.category.application.response.CategoryListResponse;
import com.moda.moda_api.category.domain.*;
import com.moda.moda_api.category.exception.TargetNotFoundException;
import com.moda.moda_api.category.infrastructure.entity.CategoryOrderEntiy;
import com.moda.moda_api.category.presentation.request.UpdateCategoryPositionRequest;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryOrderRepository categoryOrderRepository;
    private final CategoryPositionService categoryPositionService;
    private final CategoryDtoMapper categoryDtoMapper;

    /**
     * 해당 유저의 카테고리 리스트를 반환합니다.
     * @param userId
     * @return
     */
    public List<CategoryListResponse> getCategoryList(String userId) {
        UserId userIdObj = new UserId(userId);

        // 카테고리 리스트 조회
        List<Category> categoryList = categoryOrderRepository.findByUserId(userIdObj);
        
        // Position 순으로 정렬 후 반환
        return categoryPositionService.sortByPosition(categoryList).stream()
                .map(categoryDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * User의 권한을 확인하고 Category의 Position을 변경합니다.
     * 변경되는 범위에 따라 범위 내의 보드의 값을 +1 또는 -1 합니다.
     *
     * return 값은 position을 기준으로 정렬됩니다.
     * @param userId
     * @param request targetPosition은 변경될 위치에 존재하는 카테고리의 Position이어야 합니다.
     * @return
     */
    @Transactional
    public List<CategoryListResponse> updateCategoryPosition(String userId, UpdateCategoryPositionRequest request) {
        UserId userIdObj = new UserId(userId);
        CategoryId categoryIdObj = new CategoryId(request.getCategoryId());
        Position sourcePosition = new Position(request.getSourcePosition());
        Position targetPosition = new Position(request.getTargetPosition());

        // 카테고리 리스트 조회
        List<Category> categoryList = categoryOrderRepository.findByUserId(userIdObj);

        // 위치를 변경할 카테고리 가져오기
        Category targetCategory = categoryList.stream()
                .filter(category -> category.getCategoryId().equals(categoryIdObj))
                .findFirst()
                .orElseThrow(() -> new TargetNotFoundException("변경할 카테고리를 찾을 수 없습니다"));

        targetCategory.validateCurrentPosition(sourcePosition);

        // 변경되는 범위의 카테고리 위치 변경
        adjustPositions(categoryList, sourcePosition, targetPosition);

        // 타겟의 위치 변경 후 저장
        targetCategory.movePosition(targetPosition, Position.max());

        categoryOrderRepository.save(targetCategory);

        return categoryPositionService.sortByPosition(categoryList).stream()
                .map(categoryDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * private 메소드로 선언하여 별도의 트랜잭션을 생성하지 않음
     * 카테고리 리스트를 조건에 맞게 정렬합니다.
     * @param categoryList
     * @param source
     * @param target
     */
    private void adjustPositions(List<Category> categoryList, Position source, Position target) {
        // 움직이는 범위 내의 카테고리들의 position 값 변경
        categoryPositionService.adjustPositions(categoryList, source, target);

        // 카테고리 저장
        categoryOrderRepository.saveAll(categoryList);
    }

    @Transactional
    public void initCategoryPosition(String userId) {
        UserId userIdObj = new UserId(userId);

        // 이미 생성된 카테고리가 있는지 확인
        List<Category> existingCategories = categoryOrderRepository.findByUserId(userIdObj);
        if (!existingCategories.isEmpty()) {
            return;
        }

        List<CategoryOrderEntiy> categoryOrderEntities = new ArrayList<>();

        for (long position = 1L; position <= 10L; position++) {
            CategoryOrderEntiy categoryOrder = CategoryOrderEntiy.builder()
                .categoryId(position)  // Long 타입으로 직접 설정
                .userId(userId)       // String 타입으로 직접 설정
                .position((int)position)
                .build();

            categoryOrderEntities.add(categoryOrder);
        }

        // CategoryOrderEntity를 직접 저장
        categoryOrderRepository.saveAllEntities(categoryOrderEntities);
    }

}
