package com.moda.moda_api.search.application.response;

import java.util.List;
import java.util.Map;

import com.moda.moda_api.card.domain.CardContentType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResultByCardList {
    private final Map<CardContentType, List<CardDocumentListResponse>> contentResults;
    private final List<SearchTypeScore> topScores;
}
