package com.moda.moda_api.search.domain;

import com.moda.moda_api.user.domain.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CardSearchRepository {
    List<CardDocument> findAutoCompleteSuggestions(UserId userId, String exactKeyword, String prefixKeyword);

    List<CardDocument> searchByKeyword(String keyword);

    Slice<CardDocument> searchComplex(Integer typeId, UserId userId, String searchText, Pageable pageable);
}
