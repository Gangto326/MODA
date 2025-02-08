package com.moda.moda_api.search.presentation.controller;

import com.moda.moda_api.common.annotation.UserId;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.search.application.response.CardDocumentListResponse;
import com.moda.moda_api.search.application.response.SearchResultByCardList;
import com.moda.moda_api.search.application.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    /**
     * 사용자가 검색어를 입력할 때 마다 호출합니다.
     * 키워드 기반 검색어 자동 완성 기능입니다.
     * @param userId
     * @param query
     * @return
     */
    @GetMapping("/auto/{query}")
    public ResponseEntity<List<String>> autoCompleteQuery(
            @UserId String userId,
            @PathVariable String query
    ) {
        List<String> responseList = searchService.autoCompleteQuery(userId, query);
        return ResponseEntity.ok(responseList);
    }

    /**
     * 검색 후 나올 메인 페이지를 위한 다양한 콘텐츠 타입별 정보 반환
     * 메타데이터로 각 콘텐츠 타입의 우선 순위를 반환합니다
     * @param userId
     * @param query // null값일 수 있습니다.
     * @param categoryId // null값일 수 있습니다.
     * @return
     */
    @GetMapping("")
    public CompletableFuture<ResponseEntity<SearchResultByCardList>>
        searchCardDocumentListByMainPage(
                @UserId String userId,
                @RequestParam(defaultValue = "") String query,
                @RequestParam Long categoryId) {

        return searchService.searchCardDocumentListByMainPage(userId, query, categoryId)
                .thenApply(ResponseEntity::ok);
    }

    /**
     * 메인 화면에서 보여질 키워드 기준 5개의 콘텐츠를 반환합니다.
     * 현재는 조회수 기반 서치가 제외되어있습니다. (Redis와 RDBMS 사용에 따라 다른 로직 구현)
     * @param userId
     * @param keyword
     * @return
     */
    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<List<CardDocumentListResponse>> searchByKeyword(
            @UserId String userId,
            @PathVariable String keyword
    ) {
        List<CardDocumentListResponse> responseList = searchService.searchByKeyword(userId, keyword);
        return ResponseEntity.ok(responseList);
    }

    /**
     * 검색어 입력 후 상단 메뉴를 활용한 ContentType을 변경한 뒤 호출합니다.
     * @param userId
     * @param query // null값일 수 있습니다.
     * @param categoryId // null값일 수 있습니다.
     * @param typeId
     * @param page
     * @param size
     * @param sortBy
     * @param sortDirection
     * @return
     */
    @GetMapping("")
    public ResponseEntity<SliceResponseDto<CardDocumentListResponse>> search(
            @UserId String userId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam Long categoryId,
            @RequestParam Integer typeId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "15") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        SliceResponseDto<CardDocumentListResponse> responseList = searchService.search(
                userId, query, categoryId, typeId, page, size, sortBy, sortDirection
        );
        return ResponseEntity.ok(responseList);
    }
}
