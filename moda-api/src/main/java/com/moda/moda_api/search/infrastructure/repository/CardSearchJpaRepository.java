package com.moda.moda_api.search.infrastructure.repository;

import com.moda.moda_api.search.infrastructure.entity.CardDocumentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CardSearchJpaRepository extends ElasticsearchRepository<CardDocumentEntity, String> {

    /**
     * 사용자의 콘텐츠 중 적합한 콘텐츠를 가져옵니다.
     *
     * 완성이 되어가는 핵심 키워드를 자동으로 완성하고, 이미 완성 된 핵심 키워드 뒤에 연결합니다.
     * @param exactKeyword 첫 번째 키워드
     * @param prefixKeyword 마지막 매칭 될 키워드
     * @return
     */
    @Query("{" +
            "  \"bool\": {" +
            "    \"must\": [" +
            "      { \"term\": { \"userId\": \"?0\" } }," +
            "      { \"fuzzy\": { " +
            "          \"keywords\": {" +
            "              \"value\": \"?1\"," +
            "              \"fuzziness\": \"AUTO\"" +
            "          }" +
            "      }}," +
            "      { \"prefix\": { \"keywords\": \"?2\" } }" +
            "    ]" +
            "  }" +
            "}")
    List<CardDocumentEntity> findAutoCompleteSuggestions(String userId, String exactKeyword, String prefixKeyword);

    /**
     * 키워드와 정확히 일치하는 문서만 가져옵니다.
     * @param keyword
     * @return
     */
    @Query("{\"bool\": {\"must\": [{\"terms\": {\"keywords\": [?0]}}]}}")
    List<CardDocumentEntity> searchByKeyword(String keyword);

    /**
     * 검색 후 나오는 메인 페이지에 나올 데이터를 type에 맞게 가져옵니다.
     * @param typeId
     * @param userId
     * @param searchText
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
}
