package com.moda.moda_api.summary.infrastructure.api;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.moda.moda_api.summary.domain.model.CardSummaryResponse;
import com.moda.moda_api.summary.infrastructure.dto.LilysAiResponse;
import com.moda.moda_api.summary.infrastructure.service.TitleAndImageExtractor;
import com.moda.moda_api.util.exception.SummaryProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LilysAiClient {
	private final WebClient lilysWebClient;
	private final TitleAndImageExtractor titleAndImageExtractor;


	@Value("${lilys.ai.api.url}")
	private String lilysUrl;


	// private static final Map<String, Class<?>> TYPE_MAP = new HashMap<>();
	// static {
	// 	TYPE_MAP.put("blogPost", BlogPostResult.class);
	// 	TYPE_MAP.put("summaryNote", SummaryNoteResult.class);
	// 	TYPE_MAP.put("rawScript", RawScriptResult.class);
	// 	TYPE_MAP.put("shortSummary", ShortSummaryResult.class);
	// 	TYPE_MAP.put("timestamp", TimestampResult.class);
	// }

	public CompletableFuture<LilysAiResponse> getRequestId(String url) {
		System.out.println(url);
		return lilysWebClient.post()
			.uri(lilysUrl)
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

		// 1. 첫번째 Future
		// blogPost 결과를 가져오는 Future (본문 Content입니다요)
		CompletableFuture<JsonNode> contentFuture =
			getResult(requestId, "blogPost");

		// 2. 두번째 Future
		// thumbnailContent를 위한 Future (youtube면 timestamp, 아니면 shortSummary)
		String thumbnailType = url.contains("youtube.com") ? "timestamp" : "shortSummary";
		CompletableFuture<JsonNode> thumbnailFuture = getResult(requestId, thumbnailType);
		contentFuture.thenAccept(json -> {
			log.info("Received blogPost data: {}", json);
		});

		thumbnailFuture.thenAccept(json -> {
			log.info("Received thumbnail data: {}", json);
		});

		// 3. 세번째 Future
		CompletableFuture<String> titleFuture = titleAndImageExtractor.extractTitleAsync(url);

		// 4. 네번째 Future thumbNail-Url은 어떻게 저장할건데??
		CompletableFuture<String> imageUrlFuture = titleAndImageExtractor.extractImageAsync(url);


		// 모든 Future를 조합하여 하나의 CardSummaryResponse 생성
		return CompletableFuture.allOf(contentFuture, thumbnailFuture, titleFuture, imageUrlFuture)
			.thenApply(v -> {
				// 각 Future들이 완료될 때까지 join()을 호출하여 결과를 얻음
				JsonNode jsonBlogPost = contentFuture.join();
				JsonNode jsonThumbnailResult = thumbnailFuture.join();
				String title = titleFuture.join();
				String thumbnailUrl = imageUrlFuture.join();

				return CardSummaryResponse.builder()
					.typeId(url.contains("youtube.com") ? 1 : 2)
					.content(jsonBlogPost.path("data").path("data").toString())
					.thumbnailContent(jsonThumbnailResult.path("data").path("data").toString())
					.thumbnailUrl(thumbnailUrl)
					.title(title)
					.build();
			})
			.exceptionally(e -> {
				log.error("조합하는 과정에서 에러가 발생, requestId: {}", requestId, e);
				throw new SummaryProcessingException("Failed to combine summary results", e);
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
}
