package com.moda.moda_api.summary.infrastructure.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PythonAiClient {
	private final WebClient pythonWebClient;

	public AIAnalysisResponseDTO articleAnalysis(String contentItems) {
		return pythonWebClient.post()
			.uri("/summary")
			.bodyValue(contentItems)
			.retrieve()
			.bodyToMono(AIAnalysisResponseDTO.class)
			.block();
	}

	public AIAnalysisResponseDTO youtubeAnalysis(String content) {
		return pythonWebClient.post()
			.uri("/youtube")
			.bodyValue(content)
			.retrieve()
			.bodyToMono(AIAnalysisResponseDTO.class)
			.block();
	}
}

