package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.infrastructure.entity.CardDocumentEntity;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CardDocumentJpaRepository extends ElasticsearchRepository<CardDocumentEntity, String> {
    @Query("{" +
            "  \"bool\": {" +
            "    \"should\": [" +
            "      { \"match_phrase_prefix\": { \"titleCompletion\": \"?0\" } }," +
            "      { \"match_phrase_prefix\": { \"contentCompletion\": \"?0\" } }" +
            "    ]" +
            "  }" +
            "}")
    List<CardDocumentEntity> findAutoCompleteSuggestions(String prefix);

    // 키워드 정확히 일치하는 문서 검색
    List<CardDocumentEntity> findByKeywordsContaining(String keyword);
}
