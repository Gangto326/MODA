package com.moda.moda_api.card.domain;

import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CardFactory {
    public Card create(UserId userId, CategoryId categoryId,
                       Integer typeId, String urlHash,
                       String title, List<Content> contents, String thumbnailContent, String thumbnailUrl,
                       EmbeddingVector embedding) {
        return Card.builder()
                .cardId(generateCardId())
                .userId(userId)
                .categoryId(categoryId)
                .typeId(typeId)
                .urlHash(urlHash)
                .title(title)
                .contents(contents)
                .thumbnailContent(thumbnailContent)
                .thumbnailUrl(thumbnailUrl)
                .viewCount(0)
                .embedding(embedding)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Card의 ID를 생성. common의 UUID 메서드 사용 예정
    private CardId generateCardId() {
        return new CardId(UUID.randomUUID().toString());
    }
}
