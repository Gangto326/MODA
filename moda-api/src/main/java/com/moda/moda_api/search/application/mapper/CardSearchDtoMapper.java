package com.moda.moda_api.search.application.mapper;

import com.moda.moda_api.card.domain.ContentType;
import com.moda.moda_api.search.application.response.CardDocumentListResponse;
import com.moda.moda_api.search.domain.CardDocument;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CardSearchDtoMapper {

    public CardDocumentListResponse toListResponse(CardDocument cardDocument, String searchText) {
        // searchText를 공백으로 분리하여 검색 키워드 집합 생성
        Set<String> searchKeywords = new HashSet<>(Arrays.asList(searchText.split("\\s+")));

        // 도메인의 키워드 중 검색어에 없는 것들을 제외된 키워드로 설정
        String[] excludedKeywords = Arrays.stream(cardDocument.getKeywords())
                .filter(keyword -> !searchKeywords.contains(keyword))
                .toArray(String[]::new);

        return CardDocumentListResponse.builder()
                .cardId(cardDocument.getCardId().getValue())
                .type(ContentType.from(cardDocument.getTypeId()))
                .title(cardDocument.getTitle())
                .thumbnailContent(cardDocument.getThumbnailContent())
                .thumbnailUrl(cardDocument.getThumbnailUrl())
                .keywords(cardDocument.getKeywords())
                .excludedKeywords(excludedKeywords)
                .score(cardDocument.getScore())
                .build();
    }
}
