package com.moda.moda_api.card.infrastructure.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class CardEntityMapper {
    public CardEntity toEntity(Card card) {
        return CardEntity.builder()
                .cardId(card.getCardId().getValue())
                .userId(card.getUserId().getValue())
                .categoryId(card.getCategoryId().getValue())
                .typeId(card.getTypeId())
                .urlHash(card.getUrlHash())
                .title(card.getTitle())
                .content(card.getContent())
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
                .content(entity.getContent())
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

    public List<Card> toDomain(List<CardEntity> entities) {
        return entities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

}
