package com.moda.moda_api.search.infrastructure.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Document(indexName = "card")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/mappings.json")
@ToString
public class CardDocumentEntity {
    @Id
    private String id;  // cardId를 사용

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Keyword)
    private Long categoryId;

    @Field(type = FieldType.Keyword)
    private Integer typeId;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String title;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String content;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String thumbnailContent;

    @Field(type = FieldType.Keyword)
    private String thumbnailUrl;

    @Field(type = FieldType.Text, analyzer = "korean_completion")
    private String titleCompletion;  // 제목 자동완성용

    @Field(type = FieldType.Text, analyzer = "korean_completion")
    private String contentCompletion;  // 내용 자동완성용

    @Field(type = FieldType.Keyword)
    private String[] keywords;

    @Field(type = FieldType.Dense_Vector, dims = 768)
    private float[] embedding;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime createdAt;

    private Float score;
}