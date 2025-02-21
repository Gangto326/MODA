package com.moda.moda_api.card.domain;

import com.moda.moda_api.user.domain.UserId;

public interface VideoCreatorRepository {
    String getCreatorByUserId(UserId userId);
}
