package com.moda.moda_api.search.application.response;

import com.moda.moda_api.card.domain.CardContentType;

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
    private Long categoryId;
    private Integer typeId;
    private CardContentType type;
    private String title;
    private String thumbnailContent;
    private String thumbnailUrl;
    private String[] keywords;
    private String[] excludedKeywords;
    private Boolean isMine;
    private Boolean bookmark;
    private Float score;
}
