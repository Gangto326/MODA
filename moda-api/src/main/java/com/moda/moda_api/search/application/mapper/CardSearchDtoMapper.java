package com.moda.moda_api.search.application.mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
                .score(cardDocument.getScore())
                .build();
    }
}
