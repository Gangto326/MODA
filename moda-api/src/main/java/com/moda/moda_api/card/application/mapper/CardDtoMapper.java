package com.moda.moda_api.card.application.mapper;

import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardDtoMapper {
    private final ContentDtoMapper contentDtoMapper;

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
                .content(contentDtoMapper.toResponse(card.getContents()))
                .thumbnailUrl(card.getThumbnailUrl())
                .keywords(card.getKeywords())
                .createdAt(card.getCreatedAt())
                .build();
    }
}
