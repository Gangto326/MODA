package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.card.domain.CardRepository;
import com.moda.moda_api.card.exception.CardNotFoundException;
import com.moda.moda_api.card.infrastructure.entity.CardDtoEntity;
import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import com.moda.moda_api.card.infrastructure.mapper.CardEntityMapper;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {
    private final CardJpaRepository cardJpaRepository;
    private final CardEntityMapper cardEntityMapper;

    @Override
    public void save(Card card) {
        CardEntity entity = cardEntityMapper.toEntity(card);
        CardEntity savedEntity = cardJpaRepository.save(entity);

    }

    @Override
    public Slice<Card> findByUserId(UserId userId, Pageable pageable) {
        Slice<CardEntity> cardEntities = cardJpaRepository.findByUserIdAndDeletedAtIsNull(userId.getValue(), pageable);
        return cardEntities.map(cardEntityMapper::toDomain);
    }

    @Override
    public Slice<Card> findByUserIdAndCategoryId(UserId userId, CategoryId categoryId, Pageable pageable) {
        Slice<CardEntity> cardEntities = cardJpaRepository.findByUserIdAndCategoryIdAndDeletedAtIsNull(userId.getValue(),
            categoryId.getValue(), pageable);
        return cardEntities.map(cardEntityMapper::toDomain);
    }

    @Override
    public Optional<Card> findByUserIdAndCardId(UserId userId, CardId cardId) {
        Optional<CardEntity> entity = cardJpaRepository.findByUserIdAndCardId(userId.getValue(), cardId.getValue());
        return entity.map(card -> cardEntityMapper.toDomain(card));
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

    @Override
    public Optional<Card> findByUrlHash(String urlHash) {
        return cardJpaRepository.findFirstByUrlHash(urlHash)
                .map(cardEntityMapper::toDomain);
    }

    @Override
    public List<Card> findByUserIdAndTypeIdIn(UserId userIdObj, List<Integer> typeIds, Pageable pageable) {
        List<CardDtoEntity> cardEntities = cardJpaRepository.findByUserIdAndTypeIdInAndDeletedAtIsNull(
                userIdObj.getValue(), typeIds, pageable);

        return cardEntities.stream()
                .map(cardEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> findRandomCards(UserId userIdObj, LocalDateTime startDate, LocalDateTime endDate, List<Integer> typeIds, Pageable toDaysPage) {
        List<CardDtoEntity> cardEntities = cardJpaRepository.findRandomCards(
                userIdObj.getValue(), startDate, endDate, typeIds, toDaysPage);

        return cardEntities.stream()
                .map(cardEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> findByUserIdAndViewCountAndTypeIdIn(UserId userId, Integer viewCount, List<Integer> typeIds, Pageable pageable) {
        List<CardDtoEntity> cardEntities = cardJpaRepository.findByUserIdAndViewCountAndTypeIdIn(
                userId.getValue(), viewCount, typeIds, pageable);

        return cardEntities.stream()
                .map(cardEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Slice<Card> findByUserIdAndBookmarkTrueAndTypeIdAndDeletedAtIsNull(UserId userId, Integer typeId, Pageable pageable) {
        Slice<CardDtoEntity> cardEntities = cardJpaRepository.findByUserIdAndBookmarkTrueAndTypeIdAndDeletedAtIsNull(
                userId.getValue(), typeId, pageable);

        return cardEntities.map(cardEntityMapper::toDomain);
    }

    @Override
    public Boolean existsByUserIdAndUrlHashAndDeletedAtIsNull(UserId userId, String urlHash) {
        return cardJpaRepository.existsByUserIdAndUrlHashAndDeletedAtIsNull(userId.getValue(), urlHash);
    }

    @Override
    public Optional<Card> findByCardId(CardId cardId) {
        Optional<CardEntity> entity = cardJpaRepository.findByCardIdAndDeletedAtIsNull(cardId.getValue());
        return entity.map(card -> cardEntityMapper.toDomain(card));
    }

    @Override
    public List<Object[]> findCategoryExistenceByUserId(UserId userId) {
        return cardJpaRepository.findCategoryExistenceByUserId(userId.getValue());
    }
}
