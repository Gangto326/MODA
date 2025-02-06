package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.infrastructure.entity.CardDocumentEntity;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CardDocumentJpaRepository extends ElasticsearchRepository<CardDocumentEntity, String> {
    @Query("{" +
            "  \"bool\": {" +
            "    \"should\": [" +
            "      { \"match_phrase_prefix\": { \"contentCompletion\": \"?0\" } }" +
            "    ]" +
            "  }" +
            "}")
    List<CardDocumentEntity> findAutoCompleteSuggestions(String prefix);
}
