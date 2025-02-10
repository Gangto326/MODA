package com.moda.moda_api.search.infrastructure.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.search.domain.CardDocument;
import com.moda.moda_api.search.domain.CardSearchRepository;
import com.moda.moda_api.search.infrastructure.entity.CardDocumentEntity;
import com.moda.moda_api.search.infrastructure.mapper.CardDocumentMapper;
import com.moda.moda_api.user.domain.UserId;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardSearchRepositoryImpl implements CardSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations;
    private final CardSearchJpaRepository cardSearchJpaRepository;
    private final CardDocumentMapper cardDocumentMapper;

    // 카드Document Save
    @Override
    public CardDocument save(Card card) {
        CardDocumentEntity entity = cardDocumentMapper.toEntity(card);
        System.out.println(entity.toString());
        return cardDocumentMapper.toDomain(elasticsearchOperations.save(entity));
    }

    // CardDocument 리스트 생성
    @Override
    public List<CardDocument> saveAll(List<Card> cards) {
        List<CardDocumentEntity> entities = cardDocumentMapper.toEntity(cards);
        List<CardDocumentEntity> savedEntities =
            StreamSupport.stream(cardSearchJpaRepository.saveAll(entities).spliterator(), false)
                .collect(Collectors.toList()); // Iterable → List 변환

        return cardDocumentMapper.toDomain(savedEntities);
    }

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
                                .field("keywords.keyword")
                                .value(keyword)
                                .fuzziness("AUTO")
                        )));
            }
            boolQuery.must(Query.of(query -> query.bool(shouldQuery.build())));
        }

        // 마지막 입력 중인 String에 대한 와일드 카드 + fuzzy 매칭
        if (prefixKeyword != null && !prefixKeyword.isEmpty()) {
            boolQuery.must(Query.of(query -> query
                    .bool(bool -> bool
                            .should(should -> should
                                    .match(match -> match
                                            .field("keywords.ngram")
                                            .query(prefixKeyword)
                                    ))
                            .should(should -> should
                                    .fuzzy(fuzzy -> fuzzy
                                            .field("keywords.keyword")
                                            .value(prefixKeyword)
                                            .fuzziness("1")
                                    ))
                    )));
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery.build()._toQuery())
                .build();

        System.out.println(query.getQuery());
        SearchHits<CardDocumentEntity> searchHits = elasticsearchOperations.search(
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
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        boolQuery.must(Query.of(query -> query
                        .term(term -> term
                                .field("keywords.keyword")
                                .value(keyword)
                        )
                ))
                .must(Query.of(query -> query
                                .terms(terms -> terms
                                        .field("typeId")
                                        .terms(TermsQueryField.of(f -> f.value(
                                                Arrays.asList(2, 3, 4).stream()
                                                        .map(FieldValue::of)
                                                        .collect(Collectors.toList())
                                        )))
                                )
                ));

        // userId에 대한 should 조건 매칭
        boolQuery.must(Query.of(query -> query
                .bool(bool -> bool
                        .should(Query.of(q -> q
                                // userId는 term 조건으로 매칭 후 가중치 추가
                                .term(term -> term
                                        .field("userId")
                                        .value(userId.getValue())
                                        .boost(2.0f)
                                )
                        ))
                )
        ));

        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery.build()._toQuery())
                .build();

        SearchHits<CardDocumentEntity> searchHits = elasticsearchOperations.search(
                query,
                CardDocumentEntity.class
        );

        return searchHits.stream()
                .map(hit -> cardDocumentMapper.toDomain(hit.getContent()))
                .toList();
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
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // typeId에 대한 must 쿼리 매칭
        boolQuery.must(Query.of(query -> query
                .term(term -> term
                        .field("typeId")
                        .value(typeId)
                )
        ));

        // userId와 검색어에 대한 should 조건 매칭
        boolQuery.must(Query.of(query -> query
                .bool(bool -> bool
                        .should(Query.of(q -> q
                                // userId는 term 조건으로 매칭 후 가중치 추가
                                .term(term -> term
                                        .field("userId")
                                        .value(userId.getValue())
                                        .boost(2.0f)
                                )
                        ))
                        .should(Query.of(q -> q
                                // 검색어는 키워드, 제목, 콘텐츠 순으로 가중치 차등 지급
                                .multiMatch(multiMatch -> multiMatch
                                        .query(searchText)
                                        .fields("keywords.keyword^2", "title^1.5", "content")
                                )
                        ))
                )
        ));

        return executeElasticsearchSearch(boolQuery, pageable);
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
        // 쿼리 빌더
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // typeId, categoryId, userId에 대한 must 쿼리 매칭
        boolQuery.must(Query.of(query -> query
                        .term(term -> term
                                .field("typeId")
                                .value(typeId)
                        )
                ))
                .must(Query.of(query -> query
                        .term(term -> term
                                .field("categoryId")
                                .value(categoryId.getValue())
                        )
                ))
                .must(Query.of(query -> query
                        .term(term -> term
                                .field("userId")
                                .value(userId.getValue())
                        )
                ));

        return executeElasticsearchSearch(boolQuery, pageable);
    }

    /**
     * 콘텐츠 타입별 유저 및 모든 카테고리 검색
     * @param typeId
     * @param userId
     * @param pageable
     * @return
     */
    @Override
    public Slice<CardDocument> searchByAllCategoryAndType(Integer typeId, UserId userId, Pageable pageable) {
        // 쿼리 빌더
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // typeId, userId에 대한 must 쿼리 매칭
        boolQuery.must(Query.of(query -> query
                        .term(term -> term
                                .field("typeId")
                                .value(typeId)
                        )
                ))
                .must(Query.of(query -> query
                        .term(term -> term
                                .field("userId")
                                .value(userId.getValue())
                        )
                ));

        return executeElasticsearchSearch(boolQuery, pageable);
    }


    /**
     * 쿼리와 Pageable을 가지고 Slice<CardDocument>를 반환합니다.
     * @param boolQuery
     * @param pageable
     * @return
     */
    private Slice<CardDocument> executeElasticsearchSearch(
            BoolQuery.Builder boolQuery,
            Pageable pageable
    ) {
        // 네이티브 쿼리 생성
        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery.build()._toQuery())
                .withPageable(pageable)
                .build();

        // DB 탐색
        SearchHits<CardDocumentEntity> searchHits = elasticsearchOperations.search(
                query,
                CardDocumentEntity.class
        );

        // Entity를 Domain으로 변환
        List<CardDocument> content = searchHits.stream()
                .map(hit -> cardDocumentMapper.toDomain(hit.getContent()))
                .collect(Collectors.toList());

        // Slice 객체로 변경 후 반환
        return new SliceImpl<>(
                content,
                pageable,
                searchHits.getTotalHits() > (long) pageable.getPageNumber() * pageable.getPageSize()
        );
    }
}