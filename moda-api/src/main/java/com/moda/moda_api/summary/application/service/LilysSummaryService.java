package com.moda.moda_api.summary.application.service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.summary.application.dto.SummaryResultDto;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.exception.SummaryProcessingException;
import com.moda.moda_api.summary.infrastructure.api.PythonAnalysisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LilysSummaryService {
	private final LilysAiClient lilysWebClient;
	private final PythonAnalysisService pythonAnalysisService;
	private static final int MAX_ATTEMPTS = 50;
	private static final Duration POLLING_INTERVAL = Duration.ofSeconds(200);

	@Transactional
	public CompletableFuture<SummaryResultDto> summarize(String url) {
		return lilysWebClient.getRequestId(url) //requestId받고
			.thenCompose(response -> waitForCompletion(response.getRequestId()) //요약될 때까지 대기
				.thenCompose(status -> {
					log.info("Summary is ready. Status: {}", status);
					return lilysWebClient.getSummaryResults(response.getRequestId(), url) // 요약이 완성이 됐다면 요약을 가져오기
						.thenCompose(summaryResult ->
							pythonAnalysisService.analyzeSummary(summaryResult.getContent()) // AI 분석 돌리기
								.thenApply(summaryResult::updateFromDto  // 마지막으로 summaryResult 완성 시키고 보내기
								)
						);
				}));
	}

	private CompletableFuture<String> waitForCompletion(String requestId) {
		return checkStatusWithRetry(requestId, 0);

	}

	private CompletableFuture<String> checkStatusWithRetry(String requestId, int attempt) {
		if (attempt >= MAX_ATTEMPTS) {
			return CompletableFuture.failedFuture(
				new SummaryProcessingException(
					"Processing timed out after " + (MAX_ATTEMPTS * POLLING_INTERVAL.getSeconds()) + " seconds")
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
