package com.moda.moda_api.search.presentation.controller;

import com.moda.moda_api.common.annotation.UserId;
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
     * 사용자가 검색어를 입력할 때 마다 호출
     * @param userId
     * @param query
     * @return
     */
    @GetMapping("/{query}")
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
     * @param query
     * @return
     */
    @GetMapping("/main")
    public CompletableFuture<ResponseEntity<SearchResultByCardList>>
        searchCardDocumentListByMainPage(@UserId String userId, @RequestParam String query) {

        return searchService.searchCardDocumentListByMainPage(userId, query)
                .thenApply(ResponseEntity::ok);
    }


}
