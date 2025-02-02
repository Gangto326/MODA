package com.moda.moda_api.summary.application.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.summary.domain.model.CardSummaryResponse;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.util.exception.SummaryProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LilysSummaryService {
	private final LilysAiClient lilysAiClient;
	private static final int MAX_ATTEMPTS = 10;
	private static final Duration POLLING_INTERVAL = Duration.ofSeconds(30);

	@Transactional
	public CompletableFuture<CardSummaryResponse> summarize(String url) {
		return lilysAiClient.getRequestId(url)
			.thenCompose(response -> waitForCompletion(response.getRequestId())
				.thenCompose(status -> {
					log.info("Summary is ready. Status: {}", status);
					return lilysAiClient.getSummaryResults(response.getRequestId(), url);
				}));
	}

	//진짜 큰일이다 title을 못가져오고 있어 이러면 의미가 없자나 ㅠㅠㅠㅠㅠ
	private CompletableFuture<String> waitForCompletion(String requestId) {
		return checkStatusWithRetry(requestId, 0);
	}

	private CompletableFuture<String> checkStatusWithRetry(String requestId, int attempt) {
		if (attempt >= MAX_ATTEMPTS) {
			return CompletableFuture.failedFuture(
				new SummaryProcessingException("Processing timed out after " + (MAX_ATTEMPTS * POLLING_INTERVAL.getSeconds()) + " seconds")
			);
		}
		return lilysAiClient.checkStatus(requestId)
			.thenCompose(status -> {
				if ("done".equals(status)) {
					return CompletableFuture.completedFuture(status);
				}

				log.info("Summary is not ready yet. Attempt: {}", attempt + 1);

				return CompletableFuture.supplyAsync(() -> {
					try {
						Thread.sleep(POLLING_INTERVAL.toMillis());
						return checkStatusWithRetry(requestId, attempt + 1);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						throw new SummaryProcessingException("Processing was interrupted", e);
					}
				}).thenCompose(future -> future);
			});
	}

}