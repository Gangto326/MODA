package com.moda.moda_api.search.domain;

import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.user.domain.UserId;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardDocument {
    private CardId cardId;
    private UserId userId;
    private CategoryId categoryId;
    private Integer typeId;
    private String title;
    private String content;
    private String thumbnailContent;
    private String thumbnailUrl;
    private String[] keywords;
    private Float score;

    public boolean isOwnedBy(UserId userId) {
        return this.userId.equals(userId);
    }
}
