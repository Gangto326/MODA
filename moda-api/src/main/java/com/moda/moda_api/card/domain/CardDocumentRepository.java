package com.moda.moda_api.card.domain;

import java.util.List;

public interface CardDocumentRepository {
    List<String> findAutoCompleteSuggestions(String prefix);
}
