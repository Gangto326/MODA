package com.moda.moda_api.summary.infrastructure.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.summary.domain.ContentItem;
import com.moda.moda_api.summary.infrastructure.dto.PythonAnalysisDto;

@Service
public class PythonAnalysisService {

	public CompletableFuture<PythonAnalysisDto> analyzeSummary(String summaryMainContent) {
		// 더미 데이터 생성
		CategoryId categoryId = new CategoryId(1L); // 더미 카테고리 ID
		EmbeddingVector embeddingVector = new EmbeddingVector(new float[EmbeddingVector.DIMENSION]); // 임베딩 벡터 0으로 초기화
		String[] keywords = {"dummy", "example", "test"}; // 더미 키워드 배열

		// PythonAnalysisDto 생성
		PythonAnalysisDto pythonAnalysisDto = PythonAnalysisDto.builder()
			.categoryId(categoryId)
			.embeddingVector(embeddingVector)
			.keywords(keywords)
			.build();

		// CompletableFuture로 비동기적으로 반환
		return CompletableFuture.completedFuture(pythonAnalysisDto);
	}

	public CompletableFuture<PythonAnalysisDto> analyzeSummary(List<ContentItem> summaryMainContent) {
		// 더미 데이터 생성
		CategoryId categoryId = new CategoryId(1L); // 더미 카테고리 ID
		EmbeddingVector embeddingVector = new EmbeddingVector(new float[EmbeddingVector.DIMENSION]); // 임베딩 벡터 0으로 초기화
		String[] keywords = {"dummy", "example", "test"}; // 더미 키워드 배열

		// PythonAnalysisDto 생성
		PythonAnalysisDto pythonAnalysisDto = PythonAnalysisDto.builder()
			.categoryId(categoryId)
			.embeddingVector(embeddingVector)
			.keywords(keywords)
			.build();

		// CompletableFuture로 비동기적으로 반환
		return CompletableFuture.completedFuture(pythonAnalysisDto);
	}
}
