package com.moda.moda_api.summary.application.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.infrastructure.api.PythonAiClient;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiArticleRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiYoutubeRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.TitleAndContent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PythonAnalysisService {
	private final PythonAiClient pythonAiClient;

	public CompletableFuture<AIAnalysisResponseDTO> youtubeAnalyze(List<TitleAndContent> titleAndContents) {
		AIAnalysisResponseDTO aiAnalysisResponseDTO = pythonAiClient.youtubeAnalysis(AiYoutubeRequestDTO.builder()
			.paragraph(titleAndContents)
			.build());
		System.out.println(aiAnalysisResponseDTO.toString());
		return CompletableFuture.completedFuture(aiAnalysisResponseDTO);
	}

	public CompletableFuture<AIAnalysisResponseDTO> articleAnalyze(String summaryMainContent) {
		AIAnalysisResponseDTO aiAnalysisResponseDTO = pythonAiClient.articleAnalysis(
			AiArticleRequestDTO.builder().content(summaryMainContent).build());
		return CompletableFuture.completedFuture(aiAnalysisResponseDTO);
	}

}
