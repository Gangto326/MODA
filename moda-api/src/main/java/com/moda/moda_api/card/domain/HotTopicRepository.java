package com.moda.moda_api.card.domain;

import com.moda.moda_api.card.application.response.HotTopicResponse;

import java.util.List;

public interface HotTopicRepository {
    void incrementKeywordScore(String keyword);

    void savePreviousTop();

    List<String> getTopKeywords(Integer limit);

    List<HotTopicResponse> getTopKeywordsWithChange(Integer limit);
}
