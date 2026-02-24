package com.moda.moda_api.summary.infrastructure.api;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.moda.moda_api.crawling.infrastructure.crawl.TitleExtractor;
import com.moda.moda_api.summary.exception.SummaryProcessingException;
import com.moda.moda_api.summary.infrastructure.dto.LilysRequestIdResponse;
import com.moda.moda_api.summary.infrastructure.dto.LilysSummary;
import com.moda.moda_api.summary.infrastructure.dto.TitleAndContent;
import com.moda.moda_api.summary.infrastructure.mapper.JsonMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LilysAiClient {
	private final WebClient lilysWebClient;
	private final JsonMapper jsonMapper;
	private final TitleExtractor titleExtractor;

	@Value("${lilys.ai.api.url}")
	private String lilysUrl;

	public CompletableFuture<LilysRequestIdResponse> getRequestId(String url) {
		log.info("Requesting Lilys AI requestId for url: {}", url);

		return lilysWebClient.post()
			.uri(lilysUrl)
			.bodyValue(createRequestBody(url))
			.retrieve()
			.bodyToMono(LilysRequestIdResponse.class)
			.doOnError(e -> log.error("Failed to get RequestId for url: {}", url, e))
			.switchIfEmpty(Mono.error(
				new SummaryProcessingException("Received empty response from Lilys AI service")
			))
			.toFuture();
	}

	public CompletableFuture<LilysSummary> getSummaryResults(String requestId, String url) {
		log.info("getSummaryResults requestId: {}, url: {}", requestId, url);

		CompletableFuture<JsonNode> contentFuture = getResult(requestId, "summaryNote");
		CompletableFuture<JsonNode> thumbnailFuture = getResult(requestId, "shortSummary");

		return CompletableFuture.allOf(contentFuture, thumbnailFuture)
			.thenApply(v -> {
				try {
					JsonNode content = contentFuture.join();
					JsonNode thumbnail = thumbnailFuture.join();

					List<TitleAndContent> mainContent = jsonMapper.processSummaryNote(content);
					String[] timeStamps = jsonMapper.extractTimestamps(content);
					String thumbnailContent = thumbnail.path("data").path("data").path("summary").toString();
					String thumbnailUrl = getVideoId(url);

					log.info("Summary results processed: {}", mainContent);

					return LilysSummary.builder()
						.contents(mainContent)
						.thumbnailContent(thumbnailContent)
						.thumbnailUrl(thumbnailUrl)
						.typeId(1)
						.timeStamp(timeStamps)
						.build();
				} catch (Exception e) {
					throw new SummaryProcessingException("Failed to combine summary results", e);
				}
			});
	}

	// 일단 blogPost를 던져놓고 status가 뭔지 파악하는 함수
	public CompletableFuture<String> checkStatus(String requestId) {
		return getResult(requestId, "blogPost")
			.thenApply(jsonNode -> {
				String status = jsonNode.path("status").asText();
				log.info("Status check for requestId: {}, status: {}", requestId, status);
				return status;
			});
	}

	private CompletableFuture<JsonNode> getResult(String requestId, String resultType) {
		URI uri = UriComponentsBuilder.fromUriString(lilysUrl)
			.path("/{requestId}")
			.queryParam("resultType", resultType)
			.buildAndExpand(requestId)
			.toUri();

		log.info("Making request to: {}", uri);

		return lilysWebClient.get()
			.uri(UriComponentsBuilder.fromUriString(lilysUrl)
				.path("/{requestId}")
				.queryParam("resultType", resultType)
				.build(requestId))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.toFuture()
			.exceptionally(e -> {
				log.error("Error getting {} result for requestId: {}", resultType, requestId, e);
				throw new SummaryProcessingException("Failed to get " + resultType + " result", e);
			});
	}

	//요청 body만드는 과정
	private Map<String, Object> createRequestBody(String url) {
		Map<String, Object> source = new HashMap<>();
		if (url.contains("youtube.com")) {
			source.put("sourceType", "youtube_video");
		} else {
			source.put("sourceType", "webPage");
		}

		source.put("sourceUrl", url);
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("source", source);
		requestMap.put("resultLanguage", "ko");
		requestMap.put("modelType", "gpt-3.5");
		return requestMap;
	}

	private boolean isValidYoutubeUrl(String url) {
		if (url == null || url.trim().isEmpty()) {
			return false;
		}

		return url.contains("youtube.com/watch") ||
			url.contains("youtu.be/") ||
			url.contains("youtube.com/embed/") ||
			url.contains("youtube.com/v/");
	}

	// 2단계: video ID 추출
	private String getVideoId(String url) {
		try {
			// 먼저 YouTube URL인지 검증
			if (!isValidYoutubeUrl(url)) {
				throw new IllegalArgumentException("Not a valid YouTube URL");
			}

			String videoId = null;

			// URL 패턴에 따라 ID 추출
			if (url.contains("v=")) {
				videoId = url.split("v=")[1];
				if (videoId.contains("&")) {
					videoId = videoId.split("&")[0];
				}
			} else if (url.contains("youtu.be/")) {
				videoId = url.split("youtu.be/")[1];
				if (videoId.contains("?")) {
					videoId = videoId.split("\\?")[0];
				}
			} else if (url.contains("embed/")) {
				videoId = url.split("embed/")[1];
				if (videoId.contains("?")) {
					videoId = videoId.split("\\?")[0];
				}
			} else if (url.contains("v/")) {
				videoId = url.split("v/")[1];
				if (videoId.contains("?")) {
					videoId = videoId.split("\\?")[0];
				}
			}

			if (videoId == null || videoId.trim().isEmpty()) {
				throw new IllegalArgumentException("Could not extract video ID from URL");
			}

			return videoId.trim();
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid YouTube URL format: " + e.getMessage(), e);
		}
	}
}
