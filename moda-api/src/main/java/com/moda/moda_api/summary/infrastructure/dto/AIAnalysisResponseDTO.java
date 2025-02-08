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

		this.categoryId = Objects.requireNonNull(categoryId, "categoryId는 필수값입니다.");
		this.keywords = Objects.requireNonNullElse(keywords, new String[0]);
		this.thumbnailContent = Objects.requireNonNullElse(thumbnailContent, "");  // 기본값 설정
		this.content = Objects.requireNonNull(content, "content는 필수값입니다.");
		this.embeddingVector = Objects.requireNonNull(embeddingVector, "embeddingVector는 필수값입니다.");
	}

}