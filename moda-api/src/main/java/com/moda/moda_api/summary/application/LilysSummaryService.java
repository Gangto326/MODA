package com.moda.moda_api.summary.application;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.card.domain.ContentType;
import com.moda.moda_api.summary.domain.model.CardSummaryResponse;
import com.moda.moda_api.summary.domain.model.Post;
import com.moda.moda_api.summary.domain.model.Summary;
import com.moda.moda_api.summary.domain.repository.PostRepository;
import com.moda.moda_api.summary.domain.repository.SummaryRepository;
import com.moda.moda_api.summary.domain.service.ContentTypeResolver;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.BlogPostResult;
import com.moda.moda_api.summary.infrastructure.mapper.SummaryMapper;
import com.moda.moda_api.util.exception.SummaryProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LilysSummaryService {
	private final LilysAiClient lilysAiClient;
	private final SummaryMapper summaryMapper;

	@Transactional
	public CompletableFuture<CardSummaryResponse> summarize(String url) {
		return CompletableFuture.supplyAsync(() -> {
			ContentType contentType = ContentTypeResolver.resolve(url);

			List<String> resultTypes = getResultTypes(url);

			// 1. requestId를 요청 받는다.
			String requestId = lilysAiClient.getRequestId(url).getRequestId();
			log.info("requestId : {}", requestId);

			// 2. 결과가 완성이 될때까지 기다린다.
			waitForCompletion(requestId);
			log.info("받을 준비가 완료되었습니다. ");



			// 3. 모든 타입의 결과를 가져오기
			List<Object> results = resultTypes.stream()
				.map(type -> lilysAiClient.getSummaryResult(requestId, type)
					.doOnError(e -> log.error("Error getting {} result: {}", type, e.getMessage()))
					.block())
				.filter(result -> result != null)
				.collect(Collectors.toList());


			return CardSummaryResponse.builder()
				.typeId(contentType.getTypeId())
				.build();
		});
	}


	private void waitForCompletion(String requestId) {
		int attempts = 0;
		while (attempts < 10) {
			//아무 post요청이나 보내본다.
			BlogPostResult result = (BlogPostResult)lilysAiClient.getSummaryResult(requestId, "blogPost")
				.block();

			//결과의 status확인
			String status = result.getStatus();

			if (status != null && "done".equals(status)) {
				return;
			}
			try {
				log.info("요약이 완성이 될 때까지 기다려야 합니다.. ");
				Thread.sleep(Duration.ofSeconds(30).toMillis());

				attempts++;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new SummaryProcessingException("Processing was interrupted", e);
			}
		}
		throw new SummaryProcessingException("Processing timed out after " + (10 * 30) + " seconds");
	}

	private List<String> getResultTypes(String url) {
		List<String> types = new ArrayList<>();
		if (url.contains("youtube.com")) {
			types.add("timestamp");
		}
		else{
			types.add("shortSummary");
		}
		types.add("blogPost");
		return types;
	}
}