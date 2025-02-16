package com.moda.moda_api.category.presentation.controller;

import com.moda.moda_api.category.application.response.CategoryListResponse;
import com.moda.moda_api.category.application.service.CategoryService;
import com.moda.moda_api.category.presentation.request.UpdateCategoryPositionRequest;
import com.moda.moda_api.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 카테고리 리스트 반환
     * @param userId
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryListResponse>> getCategoryList(
            @UserId String userId
    ) {
        System.out.println(userId + " 여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!여기야!");
        List<CategoryListResponse> responseList = categoryService.getCategoryList(userId);

        return ResponseEntity.ok(responseList);
    }

    /**
     * 카테고리 위치 변경
     * sourcePosition은 변경할 카테고리의 Position이어야 합니다.
     * targetPosition은 변경될 위치에 존재하는 카테고리의 Position이어야 합니다.
     * @param userId
     * @param request
     * @return
     */
    @PatchMapping("")
    public ResponseEntity<List<CategoryListResponse>> updateBoardPosition(
            @UserId String userId,
            @RequestBody UpdateCategoryPositionRequest request
    ) {
        List<CategoryListResponse> responseList = categoryService.updateCategoryPosition(userId, request);
        return ResponseEntity.ok(responseList);
    }
}
