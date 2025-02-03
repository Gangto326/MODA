package com.moda.moda_api.card.application.mapper;

import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.ContentType;
import org.springframework.stereotype.Component;

@Component
public class CardDtoMapper {
    public CardListResponse toResponse(Card card) {
        return CardListResponse.builder()
                .cardId(card.getCardId().getValue())
                .boardId(card.getBoardId().getValue())
                .typeId(card.getTypeId())
                .type(ContentType.from(card.getTypeId()))
                .title(card.getTitle())
                .thumbnailContent(card.getThumbnailContent())
                .thumbnailUrl(card.getThumbnailUrl())
                .createdAt(card.getCreatedAt())
                .build();
    }

    public CardDetailResponse toDetailResponse(Card card) {
        return CardDetailResponse.builder()
                .cardId(card.getCardId().getValue())
                .boardId(card.getBoardId().getValue())
                .typeId(card.getTypeId())
                .type(ContentType.from(card.getTypeId()))
                .title(card.getTitle())
                .content(card.getContent())
                .createdAt(card.getCreatedAt())
                .build();
    }
}
