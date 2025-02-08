package com.moda.moda_api.card.domain;

import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Card save(Card card);

    Slice<Card> findByUserId(UserId userId, Pageable pageable);

    Slice<Card> findByUserIdAndCategoryId(UserId userId, CategoryId categoryId, Pageable pageable);

    Optional<Card> findByUserIdAndCardId(UserId userId, CardId cardId);

    boolean delete(Card card);

    boolean deleteAll(List<Card> cardsToDelete);

    void saveAll(List<Card> cards);

    Optional<Card> findByUrlHash(String urlHash);
}
