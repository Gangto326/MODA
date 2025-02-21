package com.moda.moda_api.card.infrastructure.entity;

import com.moda.moda_api.card.infrastructure.converter.VectorConverter;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CardDtoEntity {
    private final String cardId;
    private final String userId;
    private final Long categoryId;
    private final Integer typeId;
    private final String urlHash;
    private final String originalUrl;
    private final String title;
    private final String content;
    private final String thumbnailContent;
    private final String thumbnailUrl;
    @Convert(converter = VectorConverter.class)
    private final float[] embedding;
    private final Integer viewCount;
    private final String[] keywords;
    private final String[] subContents;
    private final Boolean bookmark;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    public CardDtoEntity(CardEntity card, String originalUrl) {
        this(
            card.getCardId(),
            card.getUserId(),
            card.getCategoryId(),
            card.getTypeId(),
            card.getUrlHash(),
            originalUrl,
            card.getTitle(),
            card.getContent(),
            card.getThumbnailContent(),
            card.getThumbnailUrl(),
            card.getEmbedding(),
            card.getViewCount(),
            card.getKeywords(),
            card.getSubContents(),
            card.getBookmark(),
            card.getCreatedAt(),
            card.getUpdatedAt(),
            card.getDeletedAt()
        );
    }
}
