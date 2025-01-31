package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardRepository;
import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import com.moda.moda_api.card.infrastructure.mapper.CardEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {
    private final CardJpaRepository cardJpaRepository;
    private final CardEntityMapper cardEntityMapper;

    @Override
    public Card save(Card card) {
        CardEntity entity = cardEntityMapper.toEntity(card);
        CardEntity savedEntity = cardJpaRepository.save(entity);
        return cardEntityMapper.toDomain(savedEntity);
    }
}
