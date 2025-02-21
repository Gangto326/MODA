package com.moda.moda_api.card.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardMainResponse {
    private List<String> topKeywords;
    private String creator;
    private Map<Long, Boolean> categories;
}
