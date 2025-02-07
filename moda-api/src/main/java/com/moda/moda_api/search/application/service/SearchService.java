package com.moda.moda_api.search.application.service;

import com.moda.moda_api.card.domain.ContentType;
import com.moda.moda_api.search.application.mapper.CardSearchDtoMapper;
import com.moda.moda_api.search.application.response.CardDocumentListResponse;
import com.moda.moda_api.search.application.response.SearchResultByCardList;
import com.moda.moda_api.search.application.response.SearchTypeScore;
import com.moda.moda_api.search.domain.CardDocument;
import com.moda.moda_api.search.domain.CardSearchRepository;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {
    private final CardSearchRepository cardSearchRepository;
    private final CardSearchDtoMapper cardSearchDtoMapper;


    /**
     * 검색어와 가장 일치하는 사용자의 게시글들의 모든 핵심 키워드 리스트를 가져옵니다.
     * 가져온 키워드들로 만들 수 있는 조합을 만들어 10개의 String 문장을 반환합니다.
     * @param userId
     * @param query
     * @return
     */
    public List<String> autoCompleteQuery(String userId, String query) {
        UserId userIdObj = new UserId(userId);

        String[] searchTerms = query.trim().split("\\s+");

        // 마지막 단어를 제외한 모든 단어는 정확히 매칭
        String exactMatch = searchTerms.length > 1 ?
                String.join(" ", Arrays.copyOfRange(searchTerms, 0, searchTerms.length - 1)) :
                searchTerms[0];

        // 마지막 단어는 prefix 매칭
        String prefixMatch = searchTerms[searchTerms.length - 1];

        List<CardDocument> documents = cardSearchRepository.findAutoCompleteSuggestions(
                userIdObj,
                exactMatch,
                prefixMatch
        );

        return documents.stream()
                .map(CardDocument::getKeywords)
                .flatMap(Arrays::stream)
                .filter(keyword -> keyword.startsWith(query))
                .distinct()
                .limit(10)
                .toList();
    }

    /**
     * 사용자의 검색 후 검색에 맞는 콘텐츠가 보일 메인 페이지 데이터를 반환합니다.
     *
     * 4(Img)의 경우는 10개, 다른 콘텐츠(1, 2, 3)의 경우 5개의 최적 컨텐츠를 반환.
     * @param userId
     * @param query
     * @return
     */
    public CompletableFuture<SearchResultByCardList> searchCardDocumentListByMainPage(
            String userId, String query
    ) {
        UserId userIdObj = new UserId(userId);
        List<Integer> targetTypes = List.of(1, 2, 3, 4);

        // IMG(4) 타입은 10개
        Map<Integer, Integer> typeSizes = Map.of(1, 5, 2, 5, 3, 5, 4, 10);

        // 각 타입별 검색을 비동기로 실행
        List<CompletableFuture<Map.Entry<ContentType, List<CardDocument>>>> futures = executeAsyncSearches(
                userIdObj, query, targetTypes, typeSizes);

        // 모든 비동기 작업이 완료되면 결과 합치기 및 메타데이터 생성
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    Map<ContentType, List<CardDocumentListResponse>> results = processSearchResults(futures, query);
                    List<SearchTypeScore> topScores = extractTopScores(results);

                    return SearchResultByCardList.builder()
                            .contentResults(results)
                            .topScores(topScores)
                            .build();
                });
    }

    /**
     * 비동기로 검색을 실행합니다.
     * @param userId
     * @param searchText
     * @param targetTypes
     * @param typeSizes
     * @return
     */
    private List<CompletableFuture<Map.Entry<ContentType, List<CardDocument>>>> executeAsyncSearches(
            UserId userId, String searchText, List<Integer> targetTypes, Map<Integer, Integer> typeSizes) {

        return targetTypes.stream()
                .map(typeId -> CompletableFuture.supplyAsync(() -> {
                    PageRequest pageRequest = PageRequest.of(0, typeSizes.get(typeId));
                    Slice<CardDocument> results = cardSearchRepository.searchComplex(
                            typeId, userId, searchText, pageRequest);
                    return Map.entry(
                            ContentType.from(typeId),
                            results.getContent().stream()
                                    .toList()
                    );
                }))
                .toList();
    }

    /**
     * 검색 결과를 처리합니다.
     * @param futures
     * @param searchText
     * @return
     */
    private Map<ContentType, List<CardDocumentListResponse>> processSearchResults(
            List<CompletableFuture<Map.Entry<ContentType, List<CardDocument>>>> futures,
            String searchText) {

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(doc -> cardSearchDtoMapper.toListResponse(doc, searchText))
                                .collect(Collectors.toList())
                ));
    }

    /**
     * 각 List내의 컨텐츠 중 최고 점수를 추출합니다.
     * @param results
     * @return
     */
    private List<SearchTypeScore> extractTopScores(
            Map<ContentType, List<CardDocumentListResponse>> results) {

        return results.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> SearchTypeScore.builder()
                        .contentType(entry.getKey())
                        .score(entry.getValue().get(0).getScore())
                        .build())
                .sorted(Comparator.comparing(SearchTypeScore::getScore).reversed())
                .toList();
    }
}
