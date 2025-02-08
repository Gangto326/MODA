package com.moda.moda_api.summary.infrastructure.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class AIAnalysisResponseDTO {

	private CategoryId categoryId;
	private String[] keywords;
	private String thumbnailContent;
	private String content;
	private EmbeddingVector embeddingVector;

	@Builder
	public AIAnalysisResponseDTO(
		@JsonProperty("categoryId") CategoryId categoryId,
		@JsonProperty("keywords") String[] keywords,
		@JsonProperty("thumbnailContent") String thumbnailContent,
		@JsonProperty("content") String content,
		@JsonProperty("embeddingVector") EmbeddingVector embeddingVector) {

		this.categoryId = categoryId;  // null 허용
		this.keywords = Objects.requireNonNullElse(keywords, new String[0]);
		this.thumbnailContent = Objects.requireNonNullElse(thumbnailContent, "");
		this.content = Objects.requireNonNullElse(content, "");
		this.embeddingVector = embeddingVector;  // null 허용
	}
}