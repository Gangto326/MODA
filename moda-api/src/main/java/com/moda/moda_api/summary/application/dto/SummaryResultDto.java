package com.moda.moda_api.summary.application.dto;

import java.util.List;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.summary.infrastructure.dto.TitleAndContent;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Builder
@ToString
public class SummaryResultDto {
	private Integer typeId;
	private String title;
	private String content;
	private String[] subContent;
	private	String[] keywords;
	private	String thumbnailContent;
	private	String thumbnailUrl;
	private	EmbeddingVector embeddingVector;
	private	CategoryId categoryId;

}