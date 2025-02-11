package com.moda.moda_api.card.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardBookmarkRequest {
    String cardId;
    Boolean isBookmark;
}
