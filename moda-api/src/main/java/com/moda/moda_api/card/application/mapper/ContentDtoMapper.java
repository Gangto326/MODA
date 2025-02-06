package com.moda.moda_api.card.application.mapper;

import com.moda.moda_api.card.application.response.ContentResponse;
import com.moda.moda_api.card.domain.Content;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ContentDtoMapper {
    public List<ContentResponse> toResponse(List<Content> contents) {
        return contents.stream()
                .map(content -> ContentResponse.builder()
                        .type(content.getType())
                        .content(content.getContent())
                        .build())
                .toList();
    }
}
