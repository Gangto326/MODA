package com.moda.moda_api.card.application.response;

import com.moda.moda_api.card.domain.CardContentType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailResponse {
    private String cardId;
    private Long categoryId;
    private Integer typeId;
    private CardContentType type;
    private String title;
    private List<ContentResponse> content;
    private String thumbnailUrl;
    private String[] keywords;
    private LocalDateTime createdAt;
}
