package com.moda.moda_api.card.infrastructure.entity;

import com.moda.moda_api.card.domain.ContentType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContentItem {
    ContentType type;
    String content;
}
