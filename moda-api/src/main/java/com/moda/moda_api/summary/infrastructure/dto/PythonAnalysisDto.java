package com.moda.moda_api.summary.infrastructure.dto;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PythonAnalysisDto {

	private CategoryId categoryId;
	private EmbeddingVector embeddingVector;
	private String[] keywords;
	private String thumbnailContent;

}
