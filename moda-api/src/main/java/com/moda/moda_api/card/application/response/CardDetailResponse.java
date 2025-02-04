package com.moda.moda_api.card.application.response;

import com.moda.moda_api.card.domain.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailResponse {
    private String cardId;
    private Long categoryId;
    private Integer typeId;
    private ContentType type;
    private String title;
    private String content;
    private String thumbnailUrl;
    private String[] keywords;
    private LocalDateTime createdAt;
}
