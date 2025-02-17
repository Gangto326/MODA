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

	public LilysRequestIdResponse getRequestId(String url) {
		System.out.println(url);

		// return CompletableFuture.completedFuture(
		// 	new LilysRequestIdResponse("0709fcc3-0baa-4da8-a984-841948466ca4")).join();


		return lilysWebClient.post()
			.uri(lilysUrl)
			.bodyValue(createRequestBody(url))  // Http메세지 Body를 만든다.
			.retrieve()  // 받을 준비가 됨.
			.bodyToMono(LilysRequestIdResponse.class)  // 응답 본문을 LilysAiResponse 클래스의 객체로 변환
			.doOnError(e -> {  // 에러 처리 실패시
				log.error("Failed to get RequestId for url: {}", url, e);
				throw new SummaryProcessingException("Failed to get RequestId from Lilys AI", e);
			})
			.switchIfEmpty(Mono.error(  // 아무것도 없을 시
				new SummaryProcessingException("Received empty response from Lilys AI service")
			))
			.block();
	}

	public LilysSummary getSummaryResults(String requestId, String url) {
		System.out.println("getSummaryResults requestId : " + requestId);
		System.out.println("getSummaryResults url : " + url);

		// 첫 번째 Future: summaryNote 가져오기
		CompletableFuture<JsonNode> contentFuture = getResult(requestId, "summaryNote");

		// 두 번째 Future: shortSummary 가져오기
		CompletableFuture<JsonNode> thumbnailFuture = getResult(requestId, "shortSummary");

		// 확인용 출력
		contentFuture.thenAccept(json -> {
			log.info("Received blogPost data: {}", json);
		});
		thumbnailFuture.thenAccept(json -> {
			log.info("Received thumbnail data: {}", json);
		});

		CompletableFuture.allOf(contentFuture, thumbnailFuture).join();

		try {
			JsonNode content = contentFuture.get();
			JsonNode thumbnail = thumbnailFuture.get();

			List<TitleAndContent> mainContent = jsonMapper.processSummaryNote(content);
			String[] timeStamps = jsonMapper.extractTimestamps(content);
			String thumbnailContent = thumbnail.path("data").path("data").path("summary").toString();
			String thumbnailUrl = getVideoId(url);

			System.out.println(mainContent.toString());

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

	private String getVideoId(String url) {
		try {
			if (url == null || url.trim().isEmpty()) {
				throw new IllegalArgumentException("URL cannot be null or empty");
			}

			String videoId = null;

			// 1. youtube.com/watch?v= 형식
			if (url.contains("youtube.com/watch?v=")) {
				videoId = url.split("v=")[1];
				if (videoId.contains("&")) {
					videoId = videoId.split("&")[0];
				}
			}
			// 2. youtu.be/ 형식 (단축 URL)
			else if (url.contains("youtu.be/")) {
				videoId = url.split("youtu.be/")[1];
				if (videoId.contains("?")) {
					videoId = videoId.split("\\?")[0];
				}
			}
			// 3. youtube.com/embed/ 형식 (임베드 URL)
			else if (url.contains("youtube.com/embed/")) {
				videoId = url.split("embed/")[1];
				if (videoId.contains("?")) {
					videoId = videoId.split("\\?")[0];
				}
			}
			// 4. youtube.com/v/ 형식 (구버전)
			else if (url.contains("youtube.com/v/")) {
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
