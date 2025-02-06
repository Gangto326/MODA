package com.moda.moda_api.card.infrastructure.mapper;

import com.moda.moda_api.card.infrastructure.entity.ContentItem;
import com.moda.moda_api.card.domain.Content;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ContentItemMapper {

    public List<ContentItem> toItem(List<Content> contents) {
        return contents.stream()
                .map(content -> ContentItem.builder()
                        .type(content.getType())
                        .content(content.getContent())
                        .build())
                .toList();
    }

    public List<Content> toDomain(List<ContentItem> contentItems) {
        return contentItems.stream()
                .map(contentItem ->  Content.builder()
                        .type(contentItem.getType())
                        .content(contentItem.getContent())
                        .build())
                .toList();
    }
}
