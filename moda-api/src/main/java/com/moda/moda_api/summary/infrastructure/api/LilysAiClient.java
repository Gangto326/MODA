package com.moda.moda_api.summary.infrastructure.api;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.moda.moda_api.summary.application.dto.SummaryResultDto;
import com.moda.moda_api.summary.infrastructure.dto.LilysAiResponse;
import com.moda.moda_api.summary.infrastructure.mapper.JsonMapper;
import com.moda.moda_api.summary.exception.SummaryProcessingException;
import com.moda.moda_api.summary.infrastructure.service.TitleExtractor;

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

	public CompletableFuture<SummaryResultDto> getSummaryResults(String requestId, String url) {

		// 첫 번째 Future: summaryNote 가져오기
		CompletableFuture<JsonNode> contentFuture = getResult(requestId, "summaryNote");

		// 두 번째 Future: shortSummary 가져오기
		CompletableFuture<JsonNode> thumbnailFuture = getResult(requestId, "shortSummary");

		// 세 번째 Future: title 가져오기
		CompletableFuture<String> titleFuture = titleExtractor.extractTitle(url);


		// 확인용 출력
		contentFuture.thenAccept(json -> {
			log.info("Received blogPost data: {}", json);
		});
		thumbnailFuture.thenAccept(json -> {
			log.info("Received thumbnail data: {}", json);
		});

		// 모든 Future를 조합하여 하나의 CardSummaryResponse 생성
		return CompletableFuture.allOf(contentFuture, thumbnailFuture, titleFuture)
			.thenApply(v -> {
				JsonNode mainContent = null;
				try {
					mainContent = jsonMapper.processSummaryNote(contentFuture.join());
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				String thumbnailContent = thumbnailFuture.join().path("data").path("data").path("summary").toString();
				String title = titleFuture.join();
				String thumbnailUrl = getThumbnailUrl(url);


				//만약 mainContent가 없으면 error가 난다.
				return SummaryResultDto.builder()
					.typeId(1)
					.content(mainContent.toString())
					.thumbnailContent(thumbnailContent)
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

	private String getThumbnailUrl(String url) {
		try {
			if (url.contains("youtube.com/watch?v=")) {
				String videoId = url.split("v=")[1].split("&")[0];  // 파라미터 제거
				return String.format("https://img.youtube.com/vi/%s/hqdefault.jpg", videoId);
			}
			throw new IllegalArgumentException("Not a YouTube URL");
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid YouTube URL format", e);
		}
	}

}
