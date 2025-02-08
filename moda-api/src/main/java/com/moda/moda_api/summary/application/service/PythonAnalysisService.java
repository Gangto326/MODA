package com.moda.moda_api.summary.application.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.application.dto.SummaryResultDto;
import com.moda.moda_api.summary.infrastructure.api.PythonAiClient;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PythonAnalysisService {
	private final PythonAiClient pythonAiClient;

	public CompletableFuture<AIAnalysisResponseDTO> youtubeAnalyze(SummaryResultDto summaryResultDto) {
		AIAnalysisResponseDTO aiAnalysisResponseDTO = pythonAiClient.youtubeAnalysis(summaryResultDto.getContent());
		return CompletableFuture.completedFuture(aiAnalysisResponseDTO);
	}

	public CompletableFuture<AIAnalysisResponseDTO> articleAnalyze(String summaryMainContent) {
		AIAnalysisResponseDTO aiAnalysisResponseDTO = pythonAiClient.articleAnalysis(summaryMainContent);
		return CompletableFuture.completedFuture(aiAnalysisResponseDTO);
	}

}
