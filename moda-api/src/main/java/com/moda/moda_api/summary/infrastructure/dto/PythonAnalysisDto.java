package com.moda.moda_api.summary.infrastructure.dto;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PythonAnalysisDto {

	private CategoryId categoryId;
	private String[] keywords;
	private String thumbnailContent;
	private String content;
	private EmbeddingVector embeddingVector;

}
