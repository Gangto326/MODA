package com.moda.moda_api.search.domain;

import com.moda.moda_api.card.domain.CardId;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardDocument {
    private CardId cardId;
    private Integer typeId;
    private String title;
    private String content;
    private String thumbnailContent;
    private String thumbnailUrl;
    private String[] keywords;
    private Float score;
}
