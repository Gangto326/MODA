package com.moda.moda_api.card.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Card save(Card card);

    Slice<Card> findByBoardUserIdAndBoardId(String userId, String boardId, Pageable pageable);

    Optional<Card> findByCardId(String cardId);

    boolean delete(Card card);

    boolean deleteAll(List<Card> cardsToDelete);
}
