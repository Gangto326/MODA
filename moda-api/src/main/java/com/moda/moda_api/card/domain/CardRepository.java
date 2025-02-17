package com.moda.moda_api.card.domain;

import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.search.domain.CardDocument;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CardRepository {
    void save(Card card);

    Slice<Card> findByUserId(UserId userId, Pageable pageable);

    Slice<Card> findByUserIdAndCategoryId(UserId userId, CategoryId categoryId, Pageable pageable);

    Optional<Card> findByUserIdAndCardId(UserId userId, CardId cardId);

    boolean delete(Card card);

    boolean deleteAll(List<Card> cardsToDelete);

    void saveAll(List<Card> cards);

    Optional<Card> findByUrlHash(String urlHash);

    List<Card> findByUserIdAndTypeIdIn(UserId userIdObj, List<Integer> typeIds, Pageable pageable);

    List<Card> findRandomCards(UserId userIdObj, LocalDateTime startDate, LocalDateTime endDate, List<Integer> typeIds, Pageable toDaysPage);

    List<Card> findByUserIdAndViewCountAndTypeIdIn(UserId userId, Integer viewCount, List<Integer> typeIds, Pageable pageable);

    Slice<Card> findByUserIdAndBookmarkTrueAndTypeIdAndDeletedAtIsNull(UserId userId, Integer typeId, Pageable pageable);

    Boolean existsByUserIdAndUrlHashAndDeletedAtIsNull(UserId userId, String urlHash);

    Optional<Card> findByCardId(CardId cardId);

    List<Object[]> findCategoryExistenceByUserId(UserId userId);

    Slice<Card> findByTypeIdAndUserId(Integer typeId, UserId userId, PageRequest pageRequest);

    Slice<Card> findByTypeIdAndCategoryIdAndUserId(Integer typeId, CategoryId categoryId, UserId userId, PageRequest pageRequest);
}
