package com.moda.moda_api.summary.application.dto;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.summary.infrastructure.dto.PythonAnalysisDto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SummaryResultDto {
	Integer typeId;
	String title;
	String content;
	String[] keyword;
	String thumbnailContent;
	String thumbnailUrl;
	EmbeddingVector embeddingVector;
	CategoryId categoryId;

	public SummaryResultDto updateFromDto(PythonAnalysisDto pythonAnalysisDto) {
		if (pythonAnalysisDto.getCategoryId() != null) {
			this.categoryId = pythonAnalysisDto.getCategoryId();
		}

		if (pythonAnalysisDto.getKeywords() != null && pythonAnalysisDto.getKeywords().length > 0) {
			this.keyword = pythonAnalysisDto.getKeywords();
		}

		if (pythonAnalysisDto.getEmbeddingVector() != null) {
			this.embeddingVector = pythonAnalysisDto.getEmbeddingVector();
		}

		if(pythonAnalysisDto.getThumbnailContent() != null){
			this.thumbnailContent = pythonAnalysisDto.getThumbnailContent();
		}

		// this를 반환하여 업데이트된 SummaryResultDto를 반환
		return this;
	}

}
