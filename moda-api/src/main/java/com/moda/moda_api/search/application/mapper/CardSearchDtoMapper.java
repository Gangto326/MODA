package com.moda.moda_api.search.application.mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.moda.moda_api.card.domain.Card;
import org.springframework.stereotype.Component;

import com.moda.moda_api.card.domain.CardContentType;
import com.moda.moda_api.search.application.response.CardDocumentListResponse;
import com.moda.moda_api.search.domain.CardDocument;
import com.moda.moda_api.user.domain.UserId;

@Component
public class CardSearchDtoMapper {

    public CardDocumentListResponse toListResponse(CardDocument cardDocument, String searchText, UserId currentUserId) {
        // searchText를 공백으로 분리하여 검색 키워드 집합 생성
        Set<String> searchKeywords = new HashSet<>(Arrays.asList(searchText.split("\\s+")));

        // 도메인의 키워드 중 검색어에 없는 것들을 제외된 키워드로 설정
        String[] excludedKeywords = Arrays.stream(cardDocument.getKeywords())
                .filter(keyword -> !searchKeywords.contains(keyword))
                .toArray(String[]::new);

        return CardDocumentListResponse.builder()
                .cardId(cardDocument.getCardId().getValue())
                .categoryId(cardDocument.getCategoryId().getValue())
                .typeId(cardDocument.getTypeId())
                .type(CardContentType.from(cardDocument.getTypeId()))
                .title(cardDocument.getTitle())
                .thumbnailContent(cardDocument.getThumbnailContent())
                .thumbnailUrl(cardDocument.getThumbnailUrl())
                .keywords(cardDocument.getKeywords())
                .excludedKeywords(excludedKeywords)
                .isMine(cardDocument.isOwnedBy(currentUserId))
                .bookmark(cardDocument.isOwnedBy(currentUserId)? cardDocument.getBookmark(): false) // 내 카드가 아니면 무조건 false 반환
                .score(cardDocument.getScore())
                .build();
    }

    public List<CardDocumentListResponse> toDocumentListResponse(List<CardDocument> cardDocuments, UserId currentUserId) {
        return cardDocuments.stream()
                .map(document -> toListResponse(document, "", currentUserId))
                .toList();
    }

    // Card 엔티티를 위한 메서드
    public CardDocumentListResponse toListResponse(Card card, UserId currentUserId) {
        return CardDocumentListResponse.builder()
                .cardId(card.getCardId().getValue())
                .categoryId(card.getCategoryId().getValue())
                .typeId(card.getTypeId())
                .type(CardContentType.from(card.getTypeId()))
                .title(card.getTitle())
                .thumbnailContent(card.getThumbnailContent())
                .thumbnailUrl(card.getThumbnailUrl())
                .keywords(card.getKeywords())
                .excludedKeywords(new String[]{})  // Card 엔티티는 검색 결과가 아니므로 제외된 키워드 없음
                .isMine(card.isOwnedBy(currentUserId))
                .bookmark(card.isOwnedBy(currentUserId)? card.getBookmark(): false) // 내 카드가 아니면 무조건 false 반환
                .score(0.0F)  // Card 엔티티는 검색 결과가 아니므로 score 없음
                .build();
    }

    // Card List를 위한 메서드
    public List<CardDocumentListResponse> toCardListResponse(List<Card> cards, UserId currentUserId) {
        return cards.stream()
                .map(card -> toListResponse(card, currentUserId))
                .toList();
    }
}
