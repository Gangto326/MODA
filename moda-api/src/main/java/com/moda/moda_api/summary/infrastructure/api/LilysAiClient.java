package com.moda.moda_api.summary.infrastructure.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.moda.moda_api.summary.domain.model.CardSummaryResponse;
import com.moda.moda_api.summary.infrastructure.dto.LilysAiResponse;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.BlogPostResult;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.RawScriptResult;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.ShortSummaryResult;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.SummaryNoteResult;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.TimestampResult;
import com.moda.moda_api.util.exception.SummaryProcessingException;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LilysAiClient {
	private WebClient webClient;
	private static final Map<String, Class<?>> TYPE_MAP = new HashMap<>();

	@Value("${lilys.ai.api.url}")
	private String apiUrl;

	@Value("${lilys.ai.api.key}")
	private String apiKey;

	// static {
	// 	TYPE_MAP.put("blogPost", BlogPostResult.class);
	// 	TYPE_MAP.put("summaryNote", SummaryNoteResult.class);
	// 	TYPE_MAP.put("rawScript", RawScriptResult.class);
	// 	TYPE_MAP.put("shortSummary", ShortSummaryResult.class);
	// 	TYPE_MAP.put("timestamp", TimestampResult.class);
	// }

	@PostConstruct  // 왜인지 모르겠지만 defaultHeader를 2번 사용하려면 PostConstruct로 해야한다.
	public void init() {
		this.webClient = WebClient.builder()
			.baseUrl(apiUrl)
			.defaultHeader("Content-Type", "application/json") // 타입 지정
			.defaultHeader("Authorization", "Bearer " + apiKey) // apiKey 넣기
			.build();
	}

	public CompletableFuture<LilysAiResponse> getRequestId(String url) {
		return webClient.post()
			.bodyValue(createRequestBody(url))  // Http메세지 Body를 만든다.
			.retrieve()  // 받을 준비가 됨.
			.bodyToMono(LilysAiResponse.class)  // 응답 본문을 LilysAiResponse 클래스의 객체로 변환
			.doOnError(e -> {  // 에러 처리 실패시
				log.error("Failed to get RequestId for url: {}", url, e);
				throw new SummaryProcessingException("Failed to get RequestId from Lilys AI", e);
			})
			.switchIfEmpty(Mono.error(  // 아무것도 없을 시
				new SummaryProcessingException("Received empty response from Lilys AI service")
			))
			.toFuture();
	}

	public CompletableFuture<CardSummaryResponse> getSummaryResults(String requestId, String url) {
		// blogPost 결과를 가져오는 Future (본문 Content입니다요)
		CompletableFuture<JsonNode> contentFuture =
			getResult(requestId, "blogPost");

		// thumbnailContent를 위한 Future (youtube면 timestamp, 아니면 shortSummary)
		String thumbnailType = url.contains("youtube.com") ? "timestamp" : "shortSummary";
		CompletableFuture<JsonNode> thumbnailFuture = getResult(requestId, thumbnailType);

		// 두 Future를 조합하여 하나의 CardSummaryResponse 생성
		return CompletableFuture.allOf(contentFuture, thumbnailFuture)
			.thenApply(v -> {
				JsonNode jsonBlogPost = contentFuture.join();
				JsonNode jsonThumbnailResult = thumbnailFuture.join();

				return CardSummaryResponse.builder()
					.typeId(url.contains("youtube.com") ? 1 : 2)
					.content(jsonBlogPost.path("data").path("data").asText())
					.thumbnailContent(jsonThumbnailResult.path("data").path("data").asText())
					.thumbnailUrl(url)
					.build();
			})
			.exceptionally(e -> {
				log.error("Error combining summary results for requestId: {}", requestId, e);
				throw new SummaryProcessingException("Failed to combine summary results", e);
			});

	}

	public CompletableFuture<String> checkStatus(String requestId) {
		return getResult(requestId, "blogPost")
			.thenApply(jsonNode -> jsonNode.path("status").asText());
	}

	private CompletableFuture<JsonNode> getResult(String requestId, String resultType) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/summaries/{requestId}")
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

}
