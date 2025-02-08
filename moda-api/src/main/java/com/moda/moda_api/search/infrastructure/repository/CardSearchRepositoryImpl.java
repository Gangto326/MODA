package com.moda.moda_api.search.infrastructure.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.search.domain.CardDocument;
import com.moda.moda_api.search.domain.CardSearchRepository;
import com.moda.moda_api.search.infrastructure.entity.CardDocumentEntity;
import com.moda.moda_api.search.infrastructure.mapper.CardDocumentMapper;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CardSearchRepositoryImpl implements CardSearchRepository {
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CardSearchJpaRepository cardSearchJpaRepository;
    private final CardDocumentMapper cardDocumentMapper;

    /**
     * 핵심 키워드를 기준으로 검색하는 동적 쿼리입니다.
     * ES와의 통신을 ElasticsearchTemplate을 이용하여 진행합니다.
     *
     * completeKeywords 리스트 내의 모든 키워드들의 fuzzy처리를 위함입니다.
     * @param completeKeywords
     * @param prefixKeyword
     * @return
     */
    @Override
    public List<CardDocument> findAutoCompleteSuggestions(UserId userId, List<String> completeKeywords, String prefixKeyword) {
        // 쿼리 빌더
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // UserId에 대한 term 쿼리 매칭
        boolQuery.must(Query.of(query -> query
                .term(term -> term
                        .field("userId")
                        .value(userId.getValue())
                )));

        // 각 키워드들에 대한 fuzzy 매칭
        if (!completeKeywords.isEmpty()) {
            BoolQuery.Builder shouldQuery = new BoolQuery.Builder();
            for (String keyword : completeKeywords) {
                shouldQuery.should(Query.of(query -> query
                        .fuzzy(fuzzy -> fuzzy
                                .field("keywords")
                                .value(keyword)
                                .fuzziness("AUTO")
                        )));
            }
            boolQuery.must(Query.of(query -> query.bool(shouldQuery.build())));
        }

        // 마지막 입력 중인 String에 대한 fuzzy 매칭
        if (prefixKeyword != null && !prefixKeyword.isEmpty()) {
            boolQuery.must(Query.of(query -> query
                    .fuzzy(fuzzy -> fuzzy
                            .field("keywords")
                            .value(prefixKeyword)
                            .fuzziness("AUTO")
                    )));
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery.build()._toQuery())
                .build();

        SearchHits<CardDocumentEntity> searchHits = elasticsearchTemplate.search(
                query,
                CardDocumentEntity.class
        );

        return searchHits.stream()
                .map(hit -> cardDocumentMapper.toDomain(hit.getContent()))
                .toList();
    }

    /**
     * 키워드 기준 검색
     * @param keyword
     * @return
     */
    @Override
    public List<CardDocument> searchByKeyword(UserId userId, String keyword, Pageable pageable) {
        return cardSearchJpaRepository.searchByKeyword(userId.getValue(), keyword, pageable).stream()
                .map(cardDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 콘텐츠 타입별 유저 및 쿼리 맞춤 검색
     * @param typeId
     * @param userId
     * @param searchText
     * @param pageable
     * @return
     */
    @Override
    public Slice<CardDocument> searchComplex(Integer typeId, UserId userId, String searchText, Pageable pageable) {
        Slice<CardDocumentEntity> cardDocumentEntities = cardSearchJpaRepository.searchByType(
                typeId, userId.getValue(), searchText, pageable
        );
        return cardDocumentEntities.map(cardDocumentMapper::toDomain);
    }

    /**
     * 콘텐츠 타입별 유저 및 카테고리 맞춤 검색
     * @param typeId
     * @param categoryId
     * @param userId
     * @param pageable
     * @return
     */
    @Override
    public Slice<CardDocument> searchByCategoryAndType(Integer typeId, CategoryId categoryId, UserId userId, Pageable pageable) {
        Slice<CardDocumentEntity> cardDocumentEntities = cardSearchJpaRepository.searchByCategoryAndType(
                typeId, categoryId.getValue(), userId.getValue(), pageable
        );
        return cardDocumentEntities.map(cardDocumentMapper::toDomain);
    }
}