package com.moda.moda_api.card.domain;

import com.moda.moda_api.card.exception.InvalidContentTypeException;
import lombok.Getter;

@Getter
public enum CardContentType {
    YOUTUBE(1),
    BLOG(2),
    NEWS(3),
    IMG(4),
    SHORTS(5),
    OTHERS(6);

    private final Integer typeId;

    CardContentType(Integer typeId) {
        this.typeId = typeId;
    }

    public static CardContentType from(Integer typeId) {
        for (CardContentType cardContentType : values()) {
            if (cardContentType.getTypeId().equals(typeId)) {
                return cardContentType;
            }
        }
        throw new InvalidContentTypeException("유효하지 않은 컨텐츠 타입입니다.");
    }
}
