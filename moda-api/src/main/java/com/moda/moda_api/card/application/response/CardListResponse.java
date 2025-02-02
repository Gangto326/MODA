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
public class CardListResponse {
    private String cardId;
    private String boardId;
    private Integer typeId;
    private ContentType type;
    private String thumbnailContent;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
