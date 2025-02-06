package com.moda.moda_api.card.application.response;

import com.moda.moda_api.card.domain.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse {
    ContentType type;
    String content;
}
