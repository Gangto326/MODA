package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.domain.CardDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardDocumentRepositoryImpl implements CardDocumentRepository {
    private final CardDocumentJpaRepository cardDocumentJpaRepository;


    @Override
    public List<String> findAutoCompleteSuggestions(String prefix) {
        return null;
    }
}
