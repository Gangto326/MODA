package com.moda.moda_api.search.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.moda.moda_api.card.domain.CardId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
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
import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
@Slf4j
public class CardSearchRepositoryImpl implements CardSearchRepository {
    private static final float MIN_SCORE = 3.5f;

    private final ElasticsearchOperations elasticsearchOperations;
    private final CardSearchJpaRepository cardSearchJpaRepository;
    private final CardDocumentMapper cardDocumentMapper;

    // 카드Document Save
    @Override
    public CardDocument save(Card card) {
        CardDocumentEntity entity = cardDocumentMapper.toEntity(card);
//        System.out.println(entity.toString());
        return cardDocumentMapper.toDomain(elasticsearchOperations.save(entity));
    }

    // 카드 Delete
    @Override
    public void deleteAllById(List<CardId> cardIds) {
        cardSearchJpaRepository.deleteAllById(cardIds.stream()
                .map(card -> card.getValue())
                .collect(Collectors.toList()));
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
     * startDate와 endDate 내의 userId와 typeIds에 일치하는 데이터를 "랜덤"으로 가져옵니다.
     * @param userId
     * @param startDate
     * @param endDate
     * @param typeIds
     * @param pageable
     * @return
     */
    @Override
    public List<CardDocument> findRandomCards(UserId userId, LocalDateTime startDate, LocalDateTime endDate, Integer[] typeIds, Pageable pageable) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder()
                // userId 필터
                .must(Query.of(query -> query
                        .term(term -> term
                                .field("userId")
                                .value(userId.getValue())
                        )
                ))
                // typeId 필터
                .must(Query.of(query -> query
                        .terms(terms -> terms
                                .field("typeId")
                                .terms(TermsQueryField.of(f -> f.value(
                                        Arrays.stream(typeIds)
                                                .map(FieldValue::of)
                                                .collect(Collectors.toList())
                                )))
                        )
                ))
                // 날짜 범위 필터
                .must(Query.of(query -> query
                        .range(range -> range
                                .date(d -> d
                                        .field("createdAt")
                                        .gte(startDate.toString())
                                        .lte(endDate.toString())
                                )
                        )
                ));

        // 랜덤 정렬과 필터를 조합한 쿼리
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .functionScore(fs -> fs
                                .query(boolQuery.build()._toQuery())
                                .functions(f -> f
                                        .randomScore(rs -> rs.seed("random-" + System.currentTimeMillis()))
                                )
                                .boostMode(FunctionBoostMode.Replace)  // 랜덤 점수로 대체
                        )
                )
                .withPageable(pageable)
                .build();

        SearchHits<CardDocumentEntity> searchHits = elasticsearchOperations.search(
                query,
                CardDocumentEntity.class
        );

        return searchHits.stream()
                .map(hit -> cardDocumentMapper.toDomain(hit.getContent()))
                .collect(Collectors.toList());
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
    public List<CardDocument> findAutoCompleteSuggestions(
            UserId userId, List<String> completeKeywords, String prefixKeyword) {

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
                boolQuery.should(Query.of(query -> query
                        .fuzzy(fuzzy -> fuzzy
                                .field("keywords.ngram")
                                .value(keyword)
                                .fuzziness("2")
                        )));
            }
            boolQuery.must(Query.of(query -> query.bool(shouldQuery.build())));
        }

        // 마지막 입력 중인 String에 대한 매칭
//        if (prefixKeyword != null && !prefixKeyword.isEmpty()) {
//            boolQuery.must(Query.of(query -> query
//                    .bool(bool -> bool
//                            .must(must -> must
//                                    .term(term -> term
//                                            .field("userId")
//                                            .value(userId.getValue())
//                                    ))
//                            .must(must -> must
//                                    .bool(b -> b
//                                            .should(should -> should
//                                                    .match(match -> match
//                                                            .field("keywords.ngram")
//                                                            .query(prefixKeyword)
//                                                            .boost(2.0f)
//                                                    ))
//                                            .should(should -> should
//                                                    .fuzzy(fuzzy -> fuzzy
//                                                            .field("keywords")
//                                                            .value(prefixKeyword)
//                                                            .fuzziness("AUTO")
//                                                            .boost(1.0f)
//                                                    ))
//                                            .minimumShouldMatch("1")
//                                    ))
//                    )));
//        }

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
     * 키워드 기준 검색. 내 것만 나옵니다.
     * @param keyword
     * @return
     */
    @Override
    public List<CardDocument> searchByKeyword(UserId userId, String keyword, List<Integer> typeIds, Pageable pageable) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        log.info("Search params - userId: {}, keyword: {}, typeIds: {}", userId.getValue(), keyword, typeIds);
        log.info("Generated ES query: {}", boolQuery.toString());

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
                                        typeIds.stream()
                                                .map(FieldValue::of)
                                                .collect(Collectors.toList())
                                )))
                        )
                ))

                // userId에 대한 must 조건 매칭
                .must(Query.of(query -> query
                            .term(term -> term
                                    .field("userId")
                                    .value(userId.getValue())
                            )
                ));

        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery.build()._toQuery())
                .build();

        log.info("Final ES query: {}", query.toString());

        SearchHits<CardDocumentEntity> searchHits = elasticsearchOperations.search(
                query,
                CardDocumentEntity.class
        );

        log.info("Total hits: {}", searchHits.getTotalHits());

        return searchHits.stream()
                .map(hit -> cardDocumentMapper.toDomain(hit.getContent()))
                .toList();
    }

    @Override
    public List<CardDocument> searchByKeywordOnlyVideo(UserId userId, String keyword, Pageable pageable) {
        return searchByKeyword(userId, keyword, Collections.singletonList(1), pageable);
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

        System.out.println(userId.getValue());

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
                                        .boost(1.7f)
                                )
                        ))
                        .should(Query.of(q -> q
                                // 검색어는 키워드, 제목, 콘텐츠 순으로 가중치 차등 지급
                                .multiMatch(multiMatch -> multiMatch
                                        .query(searchText)
//                                                .fields(
//                                                        "keywords^0.2",
//                                                        "keywords.ngram^0.25",
//                                                        "title^0.7",
//                                                        "title.ngram^0.3",
//                                                        "content^0.3",
//                                                        "content.ngram^0.003"
//                                                )
                                                .fields(
                                                        "title^3",
                                                        "title.ngram^1.5",
                                                        "keywords^2.5",
                                                        "keywords.ngram^1.25",
                                                        "content^1.5",
                                                        "content.ngram^0.5"
                                                )
                                                .type(TextQueryType.BestFields)
                                                .operator(Operator.Or)
                                )
                        ))
                        .minimumShouldMatch("1")
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
                                .boost(1000.0f)
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
                                .boost(1000.0f)
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
                .withMinScore(MIN_SCORE)
                .withTrackScores(true)  // 점수 추적 활성화
                .withSort(Sort.by(
                        Sort.Order.desc("_score")
                ))
                .build();

        // DB 탐색
        SearchHits<CardDocumentEntity> searchHits = elasticsearchOperations.search(
                query,
                CardDocumentEntity.class
        );

//        // 검색된 데이터 로깅
//        searchHits.forEach(hit -> {
//            CardDocumentEntity entity = hit.getContent();
//            System.out.println("Title: " + entity.getTitle() +
//                    ", Created At: " + entity.getCreatedAt() +
//                    ", Score: " + hit.getScore());
//        });

        // Entity를 Domain으로 변환
        List<CardDocument> content = searchHits.stream()
                .map(hit -> cardDocumentMapper.toDomain(
                        hit.getContent().toBuilder()
                                .score(Float.isNaN(hit.getScore()) ? 5.0f : hit.getScore()) // hit에서 score 가져옴
                                .build()
                ))
                .collect(Collectors.toList());

        // 실제 요청한 사이즈 (pageSize - 1)
        int originalPage = pageable.getPageNumber() + 1;
        int originalSize = pageable.getPageSize();

        // content의 크기가 originalSize보다 크면 다음 페이지 존재
        boolean hasNext = searchHits.getTotalHits() > (long)(originalPage * originalSize);

        // Slice 객체로 변경 후 반환
        return new SliceImpl<>(
                content,
                pageable,
                hasNext
        );
    }
}