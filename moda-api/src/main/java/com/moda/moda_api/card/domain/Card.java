package com.moda.moda_api.card.domain;

import java.time.LocalDateTime;

import com.moda.moda_api.card.exception.InvalidCardContentException;
import com.moda.moda_api.card.exception.InvalidCardTitleException;
import com.moda.moda_api.card.exception.UnauthorizedException;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {
    private CardId cardId;
    private UserId userId;
    private CategoryId categoryId;
    private Integer typeId;
    private String urlHash;
    private String title;
    private String content;
    private String thumbnailContent;
    private String thumbnailUrl;
    private EmbeddingVector embedding;
    private String[] keywords;
    private String[] subContents;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Builder.Default
    private Integer viewCount = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


    public void changeContent(String content) {
        validateContent(content);
        this.content = content;
    }

    public void moveCategory(CategoryId categoryId) {
        this.categoryId = categoryId;
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidCardTitleException("카드 제목은 빈 문자열일 수 없습니다.");
        }
        if (title.length() > 100) {
            throw new InvalidCardTitleException("카드 제목은 100자 이하여야 합니다.");
        }
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidCardContentException("카드 내용은 빈 문자열일 수 없습니다.");
        }
        // 가벼운 JSON 형식 검증
        String trimmedContent = content.trim();
        if (!trimmedContent.startsWith("{") || !trimmedContent.endsWith("}")) {
            throw new InvalidCardContentException("카드 내용이 올바른 JSON 형식이 아닙니다.");
        }
    }

    public void validateOwnership(UserId userId) {
        if (!this.userId.equals(userId)) {
            throw new UnauthorizedException("권한이 존재하지 않습니다.");
        }
    }
}
