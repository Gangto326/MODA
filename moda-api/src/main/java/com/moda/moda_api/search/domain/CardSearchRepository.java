package com.moda.moda_api.search.domain;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public interface CardSearchRepository {
    List<CardDocument> findAutoCompleteSuggestions(UserId userId, List<String> completeKeywords, String prefixKeyword);

    List<CardDocument> searchByKeyword(UserId userId, String keyword, List<Integer> typeIds, Pageable pageable);

    Slice<CardDocument> searchComplex(Integer typeId, UserId userId, String searchText, Pageable pageable);

    Slice<CardDocument> searchByCategoryAndType(Integer typeId, CategoryId categoryId, UserId userId,
        Pageable pageable);

    Slice<CardDocument> searchByAllCategoryAndType(Integer typeId, UserId userId, Pageable pageable);

    CardDocument save(Card card);

    void deleteAllById(List<CardId> cardIds);

    List<CardDocument> saveAll(List<Card> cards);

    List<CardDocument> findRandomCards(UserId userId, LocalDateTime startDate, LocalDateTime endDate, Integer[] typeIds, Pageable pageable);

    List<CardDocument> searchByKeywordOnlyVideo(UserId userId, String keyword, Pageable pageable);
}
