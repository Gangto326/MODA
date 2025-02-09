package com.moda.moda_api.card.application.mapper;

import org.springframework.stereotype.Component;

import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardContentType;

@Component
public class CardDtoMapper {
    public CardListResponse toResponse(Card card) {
        return CardListResponse.builder()
                .cardId(card.getCardId().getValue())
                .categoryId(card.getCategoryId().getValue())
                .typeId(card.getTypeId())
                .type(CardContentType.from(card.getTypeId()))
                .title(card.getTitle())
                .thumbnailContent(card.getThumbnailContent())
                .thumbnailUrl(card.getThumbnailUrl())
                .keywords(card.getKeywords())
                .createdAt(card.getCreatedAt())
                .build();
    }

    public CardDetailResponse toDetailResponse(Card card) {
        return CardDetailResponse.builder()
                .cardId(card.getCardId().getValue())
                .categoryId(card.getCategoryId().getValue())
                .typeId(card.getTypeId())
                .type(CardContentType.from(card.getTypeId()))
                .title(card.getTitle())
                .content(card.getContent())
                .thumbnailUrl(card.getThumbnailUrl())
                .keywords(card.getKeywords())
                .createdAt(card.getCreatedAt())
                .build();
    }
}
