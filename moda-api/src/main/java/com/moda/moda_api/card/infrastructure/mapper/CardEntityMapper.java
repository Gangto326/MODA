package com.moda.moda_api.card.infrastructure.mapper;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardEntityMapper {
    private final ContentItemMapper contentItemMapper;

    public CardEntity toEntity(Card card) {
        return CardEntity.builder()
                .cardId(card.getCardId().getValue())
                .userId(card.getUserId().getValue())
                .categoryId(card.getCategoryId().getValue())
                .typeId(card.getTypeId())
                .urlHash(card.getUrlHash())
                .title(card.getTitle())
                .content(contentItemMapper.toItem(card.getContents()))
                .thumbnailContent(card.getThumbnailContent())
                .thumbnailUrl(card.getThumbnailUrl())
                .embedding(card.getEmbedding().getValues())
                .viewCount(card.getViewCount())
                .keywords(card.getKeywords())
                .createdAt(card.getCreatedAt())
                .updatedAt(card.getUpdatedAt())
                .deletedAt(card.getDeletedAt())
                .build();
    }

    public Card toDomain(CardEntity entity) {
        return Card.builder()
                .cardId(new CardId(entity.getCardId()))
                .userId(new UserId(entity.getUserId()))
                .categoryId(new CategoryId(entity.getCategoryId()))
                .typeId(entity.getTypeId())
                .urlHash(entity.getUrlHash())
                .title(entity.getTitle())
                .contents(contentItemMapper.toDomain(entity.getContent()))
                .thumbnailContent(entity.getThumbnailContent())
                .thumbnailUrl(entity.getThumbnailUrl())
                .embedding(new EmbeddingVector(entity.getEmbedding()))
                .viewCount(entity.getViewCount())
                .keywords(entity.getKeywords())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
