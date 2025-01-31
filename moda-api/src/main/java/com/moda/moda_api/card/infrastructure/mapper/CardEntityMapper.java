package com.moda.moda_api.card.infrastructure.mapper;

import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import org.springframework.stereotype.Component;

@Component
public class CardEntityMapper {
    public CardEntity toEntity(Card card) {
        return CardEntity.builder()
                .cardId(card.getCardId().getValue())
                .boardId(card.getBoardId().getValue())
                .typeId(card.getTypeId())
                .urlHash(card.getUrlHash())
                .title(card.getTitle())
                .content(card.getContent())
                .thumbnailContent(card.getThumbnailContent())
                .thumbnailUrl(card.getThumbnailUrl())
                .embedding(card.getEmbedding().getValues())
                .viewCount(card.getViewCount())
                .createdAt(card.getCreatedAt())
                .updatedAt(card.getUpdatedAt())
                .deletedAt(card.getDeletedAt())
                .build();
    }

    public Card toDomain(CardEntity entity) {
        return Card.builder()
                .cardId(new CardId(entity.getCardId()))
                .boardId(new BoardId(entity.getBoardId()))
                .typeId(entity.getTypeId())
                .urlHash(entity.getUrlHash())
                .title(entity.getTitle())
                .content(entity.getContent())
                .thumbnailContent(entity.getThumbnailContent())
                .thumbnailUrl(entity.getThumbnailUrl())
                .embedding(new EmbeddingVector(entity.getEmbedding()))
                .viewCount(entity.getViewCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}
