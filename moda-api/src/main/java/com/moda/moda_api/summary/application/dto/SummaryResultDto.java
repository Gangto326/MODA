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
	private List<TitleAndContent> titleAndContents;

	public SummaryResultDto updateFromDto(AIAnalysisResponseDTO aiAnalysisResponseDTO) {
		if (aiAnalysisResponseDTO.getCategoryId() != null) {
			this.categoryId = aiAnalysisResponseDTO.getCategoryId();
		}

		if (aiAnalysisResponseDTO.getKeywords() != null && aiAnalysisResponseDTO.getKeywords().length > 0) {
			this.keywords = aiAnalysisResponseDTO.getKeywords();
		}

		if (aiAnalysisResponseDTO.getEmbeddingVector() != null) {
			this.embeddingVector = aiAnalysisResponseDTO.getEmbeddingVector();
		}

		// thumbnailContent가 null이거나 빈 문자열인 경우에만 업데이트
		if (aiAnalysisResponseDTO.getThumbnailContent() != null &&
			(this.thumbnailContent == null || this.thumbnailContent.trim().isEmpty())) {
			this.thumbnailContent = aiAnalysisResponseDTO.getThumbnailContent();
		}

		return this;
	}
}