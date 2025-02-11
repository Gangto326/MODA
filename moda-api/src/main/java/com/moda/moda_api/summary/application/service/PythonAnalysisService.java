package com.moda.moda_api.summary.application.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.infrastructure.api.PythonAiClient;
import com.moda.moda_api.summary.infrastructure.api.YoutubeApiClient;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiArticleRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiYoutubeRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.TitleAndContent;
import com.moda.moda_api.summary.infrastructure.dto.YoutubeAPIResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PythonAnalysisService {
	private final PythonAiClient pythonAiClient;
	private final YoutubeApiClient youtubeApiClient;

	public AIAnalysisResponseDTO youtubeAnalyze(List<TitleAndContent> titleAndContents) {
		// AI 분석 키워드를 받고.

		return pythonAiClient.youtubeAnalysis(AiYoutubeRequestDTO.builder()
			.paragraph(titleAndContents)
			.build());
	}

	public AIAnalysisResponseDTO articleAnalyze(String summaryMainContent) {
		return pythonAiClient.articleAnalysis(
			AiArticleRequestDTO.builder().content(summaryMainContent).build());
	}

}
