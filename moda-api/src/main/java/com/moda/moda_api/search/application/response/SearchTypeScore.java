package com.moda.moda_api.search.application.response;

import com.moda.moda_api.card.domain.ContentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchTypeScore {
    private final ContentType contentType;
    private final Float score;
}