package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardRepository;
import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import com.moda.moda_api.card.infrastructure.mapper.CardEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Slice<Card> findByBoardUserIdAndBoardId(String userId, String boardId, Pageable pageable) {
        Slice<CardEntity> cardEntities = cardJpaRepository.findByBoardUserIdAndBoardId(userId, boardId, pageable);
        return cardEntities.map(cardEntityMapper::toDomain);
    }

    @Override
    public Optional<Card> findByCardId(String cardId) {
        return cardJpaRepository.findById(cardId)
                .map(cardEntityMapper::toDomain);
    }

    @Override
    public boolean delete(Card card) {
        CardEntity entity = cardEntityMapper.toEntity(card);
        cardJpaRepository.delete(entity);
        return true;
    }

    @Override
    public boolean deleteAll(List<Card> cardsToDelete) {
        List<CardEntity> cardEntities = cardsToDelete.stream()
                .map(cardEntityMapper::toEntity)
                .collect(Collectors.toList());

        cardJpaRepository.deleteAll(cardEntities);
        return true;
    }

    @Override
    public void saveAll(List<Card> cards) {
        List<CardEntity> cardEntities = cards.stream()
                .map(cardEntityMapper::toEntity)
                .collect(Collectors.toList());

        cardJpaRepository.saveAll(cardEntities);
    }
}
