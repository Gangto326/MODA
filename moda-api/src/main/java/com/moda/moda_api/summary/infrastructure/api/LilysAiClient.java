package com.moda.moda_api.summary.infrastructure.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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

	static {
		TYPE_MAP.put("blogPost", BlogPostResult.class);
		TYPE_MAP.put("summaryNote", SummaryNoteResult.class);
		TYPE_MAP.put("rawScript", RawScriptResult.class);
		TYPE_MAP.put("shortSummary", ShortSummaryResult.class);
		TYPE_MAP.put("timestamp", TimestampResult.class);
	}

	@PostConstruct  // 왜인지 모르겠지만 defaultHeader를 2번 사용하려면 PostConstruct로 해야한다.
	public void init() {
		this.webClient = WebClient.builder()
			.baseUrl(apiUrl)
			.defaultHeader("Content-Type", "application/json") // 타입 지정
			.defaultHeader("Authorization", "Bearer " + apiKey) // apiKey 넣기
			.build();
	}

	public Mono<LilysAiResponse> getRequestId(String url) { // Mono를 사용하면 비동기 처리가 가능해집니다.
		return webClient.post()
			.bodyValue(createRequestBody(url))// Http메세지 Body를 만든다.
			.retrieve()// 받을 준비가 되었습니다.
			.bodyToMono(LilysAiResponse.class)// 응답 본문을 LilysAiResponse 클래스의 객체로 변환
			.doOnError(e -> { // 에러 처리
				log.error("RequestId를 받는데 실패함 ", e);
				throw new SummaryProcessingException("릴리스 AI를 통해 RequestId를 받아오는 과정에서 예외", e);
			})
			.switchIfEmpty(Mono.error(
				new SummaryProcessingException("Received null response body from AI service")
			));
	}

	public <T> Mono<T> getSummaryResult(String requestId, String resultType) {
		Class<?> resultClass = TYPE_MAP.get(resultType); // class 받아오기

		if (resultClass == null) { // 해당 class 없으면 에러 처리
			return Mono.error(new IllegalArgumentException("Unsupported result type: " + resultType));
		}

		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/{requestId}")
				.queryParam("resultType", resultType)
				.build(requestId))
			.retrieve()
			.bodyToMono((Class<T>) resultClass)// Type별로 클래스 받기.
			.doOnError(e -> {
				log.error("Error getting summary result for requestId: {}, resultType: {}",
					requestId, resultType, e);
				throw new SummaryProcessingException("Failed to get summary result", e);
			});
	}

	//요청 body만드는 과정
	private Map<String, Object> createRequestBody(String url) {
		Map<String, Object> source = new HashMap<>();
		if(url.contains("youtube.com")) {
			source.put("sourceType", "youtube_video");
		}else{
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
