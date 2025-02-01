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
    private String boardId;
    private Integer typeId;

    @Builder.Default
    private ContentType type = ContentType.from(typeId);

    private String thumbnailContent;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
