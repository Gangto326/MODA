package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.domain.CardDocumentRepository;
import com.moda.moda_api.card.infrastructure.entity.CardDocumentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardDocumentRepositoryImpl implements CardDocumentRepository {
    private final CardDocumentJpaRepository cardDocumentJpaRepository;

    @Override
    public List<String> findAutoCompleteSuggestions(String prefix) {
        return cardDocumentJpaRepository.findAutoCompleteSuggestions(prefix)
                .stream()
                .map(CardDocumentEntity::getContent)
                .limit(5)  // 상위 5개 결과만
                .toList();
    }
}