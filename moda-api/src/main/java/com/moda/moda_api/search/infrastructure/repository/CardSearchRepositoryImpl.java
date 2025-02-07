package com.moda.moda_api.search.infrastructure.repository;

import com.moda.moda_api.search.domain.CardDocument;
import com.moda.moda_api.search.domain.CardSearchRepository;
import com.moda.moda_api.search.infrastructure.entity.CardDocumentEntity;
import com.moda.moda_api.search.infrastructure.mapper.CardDocumentMapper;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class CardSearchRepositoryImpl implements CardSearchRepository {
    private final CardSearchJpaRepository cardSearchJpaRepository;
    private final CardDocumentMapper cardDocumentMapper;

    /**
     * 핵심 키워드를 기준으로 검색
     * @param exactKeyword
     * @param prefixKeyword
     * @return
     */
    @Override
    public List<CardDocument> findAutoCompleteSuggestions(UserId userId, String exactKeyword, String prefixKeyword) {
        return cardSearchJpaRepository.findAutoCompleteSuggestions(
                userId.getValue(), exactKeyword, prefixKeyword
                ).stream()
                .map(cardDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 키워드 기준 검색
     * @param keyword
     * @return
     */
    @Override
    public List<CardDocument> searchByKeyword(String keyword) {
        return cardSearchJpaRepository.searchByKeyword(keyword).stream()
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
}