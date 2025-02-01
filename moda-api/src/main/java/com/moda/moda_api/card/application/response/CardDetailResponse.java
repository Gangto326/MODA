package com.moda.moda_api.card.application.response;

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
    private String boardId;
    private Integer typeId;
    private String thumbnailContent;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
