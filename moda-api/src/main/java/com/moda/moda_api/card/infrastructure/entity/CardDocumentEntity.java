package com.moda.moda_api.card.infrastructure.entity;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "card")
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/mappings.json")
public class CardDocumentEntity {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String content;

    @Field(type = FieldType.Text, analyzer = "korean_completion")
    private String contentCompletion;
}
