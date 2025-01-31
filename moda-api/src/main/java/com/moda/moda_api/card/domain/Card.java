package com.moda.moda_api.card.domain;

import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.card.exception.InvalidCardTitleException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {
    private CardId cardId;
    private BoardId boardId;
    private Integer typeId;
    private String urlHash;
    private String title;
    private String content;
    private String thumbnailContent;
    private String thumbnailUrl;
    private Integer viewCount;
    private EmbeddingVector embedding;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidCardTitleException("카드명은 빈 문자열일 수 없습니다.");
        }
        if (title.length() > 100) {
            throw new InvalidCardTitleException("카드명은 100자 이하여야 합니다.");
        }
    }
}
