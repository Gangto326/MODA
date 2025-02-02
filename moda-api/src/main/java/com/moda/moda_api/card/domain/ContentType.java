package com.moda.moda_api.card.domain;

import com.moda.moda_api.card.exception.InvalidContentTypeException;
import lombok.Getter;

@Getter
public enum ContentType {
    YOUTUBE(1),
    BLOG(2),
    NEWS(3),
    IMG(4),
    SHORTS(5),
    OTHERS(6);

    private final Integer typeId;

    ContentType(Integer typeId) {
        this.typeId = typeId;
    }

    public static ContentType from(Integer typeId) {
        for (ContentType contentType : values()) {
            if (contentType.getTypeId().equals(typeId)) {
                return contentType;
            }
        }
        throw new InvalidContentTypeException("유효하지 않은 컨텐츠 타입입니다.");
    }

    public static Integer fromString(ContentType contentType) {
        try {
            ContentType contentType = ContentType.valueOf(contentTypeStr.toUpperCase());
            return contentType.getTypeId();
        } catch (IllegalArgumentException e) {
            throw new InvalidContentTypeException("유효하지 않은 컨텐츠 타입 문자열입니다: " + contentTypeStr);
        }
    }
}
