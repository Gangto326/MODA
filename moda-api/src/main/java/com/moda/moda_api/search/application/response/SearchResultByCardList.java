package com.moda.moda_api.search.application.response;

import com.moda.moda_api.card.domain.ContentType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class SearchResultByCardList {
    private final Map<ContentType, List<CardDocumentListResponse>> contentResults;
    private final List<SearchTypeScore> topScores;
}
