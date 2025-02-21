package com.moda.moda_api.search.application.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class MainResultByCardList {
    private final Map<String, List<CardDocumentListResponse>> contentResults;
}
