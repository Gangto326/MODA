package com.moda.moda_api.summary.application.service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.summary.domain.model.CardSummaryResponse;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.util.exception.SummaryProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class LilysSummaryService {
	private final LilysAiClient lilysWebClient;
	private static final int MAX_ATTEMPTS = 50;
	private static final Duration POLLING_INTERVAL = Duration.ofSeconds(200);

	@Transactional
	public CompletableFuture<CardSummaryResponse> summarize(String url) {
		return lilysWebClient.getRequestId(url)
			.thenCompose(response -> waitForCompletion(response.getRequestId())
				.thenCompose(status -> {
					log.info("Summary is ready. Status: {}", status);
					return lilysWebClient.getSummaryResults(response.getRequestId(), url);
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

		return lilysWebClient.checkStatus(requestId)
			.thenCompose(status -> {
				if ("done".equals(status)) {
					return CompletableFuture.completedFuture(status);
				}
				if ("pending".equals(status)) {
					log.info("Summary is still pending. Will retry in 15 seconds. Attempt: {}", attempt + 1);
					return CompletableFuture.supplyAsync(
						() -> checkStatusWithRetry(requestId, attempt + 1),
						CompletableFuture.delayedExecutor(15, TimeUnit.SECONDS)
					).thenCompose(future -> future);
				}
				return CompletableFuture.failedFuture(
					new SummaryProcessingException("Unexpected status: " + status)
				);
			});
	}
}
//0d720162-1bc9-43ac-9258-c2edc9d8f38d
