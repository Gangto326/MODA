package com.moda.moda_api.card.infrastructure.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moda.moda_api.card.infrastructure.entity.ContentItem;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;

@Converter
public class ContentItemListConverter implements AttributeConverter<List<ContentItem>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ContentItem> contents) {
        //Information 객체 -> Json 문자열로 변환
        try {
            return objectMapper.writeValueAsString(contents);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ContentItem> convertToEntityAttribute(String jsonString) {
        //Json 문자열 Information 객체로 변환
        try {
            return objectMapper.readValue(jsonString,
                    new TypeReference<List<ContentItem>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
