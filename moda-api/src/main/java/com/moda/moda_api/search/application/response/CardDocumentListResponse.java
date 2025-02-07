package com.moda.moda_api.search.application.response;

import com.moda.moda_api.card.domain.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDocumentListResponse {
    private String cardId;
    private ContentType type;
    private String title;
    private String thumbnailContent;
    private String thumbnailUrl;
    private String[] keywords;
    private String[] excludedKeywords;
    private Float score;
}
