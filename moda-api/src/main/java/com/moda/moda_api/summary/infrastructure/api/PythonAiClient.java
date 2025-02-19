package com.moda.moda_api.summary.infrastructure.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiArticleRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiImageRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiYoutubeRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PythonAiClient {
	private final WebClient pythonWebClient;


	public AIAnalysisResponseDTO articleAnalysis(AiArticleRequestDTO aiArticleRequestDTO) {
		return pythonWebClient.post()
			.uri("summary/post")
			.bodyValue(aiArticleRequestDTO)
			.retrieve()
			.bodyToMono(AIAnalysisResponseDTO.class)
			.block();

	}

	public AIAnalysisResponseDTO youtubeAnalysis(AiYoutubeRequestDTO aiYoutubeRequestDTO) {
		return pythonWebClient.post()
			.uri("summary/youtube")
			.bodyValue(aiYoutubeRequestDTO)
			.retrieve()
			.bodyToMono(AIAnalysisResponseDTO.class)
			.block();
	}

	public AIAnalysisResponseDTO imageAnalysis(AiImageRequestDTO aiImageRequestDTO) {
		return pythonWebClient.post()
			.uri("summary/image")
			.bodyValue(aiImageRequestDTO)
			.retrieve()
			.bodyToMono(AIAnalysisResponseDTO.class)
			.block();
	}

}

