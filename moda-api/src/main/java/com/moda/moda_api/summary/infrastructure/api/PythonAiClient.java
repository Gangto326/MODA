package com.moda.moda_api.summary.infrastructure.api;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiArticleRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiImageRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiYoutubeRequestDTO;
import com.moda.moda_api.summary.infrastructure.dto.CrawlResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PythonAiClient {
	private final WebClient pythonWebClient;


	public CompletableFuture<AIAnalysisResponseDTO> articleAnalysis(AiArticleRequestDTO aiArticleRequestDTO) {
		return pythonWebClient.post()
			.uri("summary/post")
			.bodyValue(aiArticleRequestDTO)
			.retrieve()
			.bodyToMono(AIAnalysisResponseDTO.class)
			.timeout(Duration.ofSeconds(30))
			.toFuture();
	}

	public CompletableFuture<AIAnalysisResponseDTO> youtubeAnalysis(AiYoutubeRequestDTO aiYoutubeRequestDTO) {
		return pythonWebClient.post()
			.uri("summary/youtube")
			.bodyValue(aiYoutubeRequestDTO)
			.retrieve()
			.bodyToMono(AIAnalysisResponseDTO.class)
			.timeout(Duration.ofSeconds(30))
			.toFuture();
	}

	public CompletableFuture<AIAnalysisResponseDTO> imageAnalysis(AiImageRequestDTO aiImageRequestDTO) {
		return pythonWebClient.post()
			.uri("summary/image")
			.bodyValue(aiImageRequestDTO)
			.retrieve()
			.bodyToMono(AIAnalysisResponseDTO.class)
			.timeout(Duration.ofSeconds(30))
			.toFuture();
	}

	public CompletableFuture<CrawlResponseDTO> crawlAndAnalyze(String url) {
		return pythonWebClient.post()
			.uri("crawl")
			.bodyValue(Map.of("url", url))
			.retrieve()
			.bodyToMono(CrawlResponseDTO.class)
			.timeout(Duration.ofSeconds(60))
			.toFuture();
	}

}

