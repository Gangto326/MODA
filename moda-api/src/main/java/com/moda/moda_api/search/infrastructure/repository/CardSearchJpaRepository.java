package com.moda.moda_api.search.infrastructure.repository;

import com.moda.moda_api.search.infrastructure.entity.CardDocumentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;


@EnableElasticsearchRepositories
public interface CardSearchJpaRepository extends ElasticsearchRepository<CardDocumentEntity, String> {

    /**
     * 키워드가 정확히 일치하는 동영상, 블로그, 뉴스 카드만 가져옵니다.
     * @param keyword
     * @return
     */
    @Query("{" +
            "  \"bool\": {" +
            "    \"must\": [" +
            "      { \"terms\": { \"keywords\": [?1] } }," +
            "      { \"terms\": { \"typeId\": [1, 2, 3] } }" +
            "    ]," +
            "    \"should\": [" +
            "      { \"term\": { \"userId\": \"?0\", \"boost\": 2.0 } }" +
            "    ]" +
            "  }" +
            "}")
    List<CardDocumentEntity> searchByKeyword(String userId, String keyword, Pageable pageable);

    /**
     * 검색 후 나오는 데이터를 type에 맞게 가져옵니다.
     * @param typeId ContentType
     * @param userId
     * @param searchText 검색어
     * @param pageable
     * @return
     */
    @Query("{" +
            "  \"query\": {" +
            "    \"bool\": {" +
            "      \"must\": [" +
            "        { \"term\": { \"typeId\": ?0 } }," +  // 특정 타입만
            "        {" +
            "          \"bool\": {" +
            "            \"should\": [" +
            "              { \"term\": { \"userId\": \"?1\", \"boost\": 2.0 } }," +
            "              { \"multi_match\": { \"query\": \"?2\", \"fields\": [\"keywords^2\", \"title^1.5\", \"content\"] } }" +
            "            ]" +
            "          }" +
            "        }" +
            "      ]" +
            "    }" +
            "  }" +
            "}")
    Slice<CardDocumentEntity> searchByType(Integer typeId, String userId, String searchText, Pageable pageable);

    /**
     * 각 카테고리별 데이터를 type에 맞게 가져옵니다.
     * @param typeId
     * @param categoryId
     * @param userId
     * @param pageable
     * @return
     */
    @Query("{\"query\": {\"bool\": {\"must\": [{ \"term\": { \"typeId\": \"?0\"}}, { \"term\": { \"categoryId\": \"?1\"}}, { \"term\": { \"userId\": \"?2\"}}]}}}")
    Slice<CardDocumentEntity> searchByCategoryAndType(Integer typeId, Long categoryId, String userId, Pageable pageable);
}
