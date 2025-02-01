package com.moda.moda_api.card.application.mapper;

import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.domain.Card;

public class CardDtoMapper {
    public CardDetailResponse toResponse(Card card) {
        return CardDetailResponse.builder()
                .cardId(card.getCardId().getValue())
                .boardId(card.getBoardId().getValue())
                .typeId(card.getTypeId())
                .thumbnailContent(card.getThumbnailContent())
                .thumbnailUrl(card.getThumbnailUrl())
                .createdAt(card.getCreatedAt())
                .build();
    }
}
