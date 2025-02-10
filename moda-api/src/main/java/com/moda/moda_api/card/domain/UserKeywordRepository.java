package com.moda.moda_api.card.domain;

import com.moda.moda_api.user.domain.UserId;

import java.util.List;

public interface UserKeywordRepository {
    void saveKeywords(UserId userId, String[] keywords);

    List<String> getTopKeywords(UserId userId, int limit);
}
