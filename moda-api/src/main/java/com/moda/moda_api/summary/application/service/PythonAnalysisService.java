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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PythonAnalysisService {
	private final PythonAiClient pythonAiClient;

	public CompletableFuture<AIAnalysisResponseDTO> youtubeAnalyze(List<TitleAndContent> titleAndContents) {
		log.info("파이썬 분석하기직전");
		return pythonAiClient.youtubeAnalysis(AiYoutubeRequestDTO.builder()
			.paragraph(titleAndContents)
			.build());
	}

	public CompletableFuture<AIAnalysisResponseDTO> articleAnalyze(String summaryMainContent) {
		return pythonAiClient.articleAnalysis(
			AiArticleRequestDTO.builder().content(summaryMainContent).build());
	}

}
