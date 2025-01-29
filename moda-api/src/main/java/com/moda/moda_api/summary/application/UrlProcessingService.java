package com.moda.moda_api.summary.application;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.infrastructure.dto.LilysAiResponse;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.BlogPostResult;
import com.moda.moda_api.util.exception.SummaryProcessingException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UrlProcessingService {
	private static final List<String> RESULT_TYPES = Arrays.asList(
		"blogPost", "summaryNote", "rawScript", "shortSummary", "timestamp"
	);

	private final LilysAiClient lilysAiClient;

	public UrlProcessingService(LilysAiClient lilysAiClient) {
		this.lilysAiClient = lilysAiClient;
	}

	public CompletableFuture<Object> processUrl(String url, String resultType) {
		return CompletableFuture.supplyAsync(() -> {

			String requestId = lilysAiClient.getRequestId(url)
				.block().getRequestId(); // requestId를 얻는 과정.

			waitForCompletion(requestId);

			return getSummaryAsync(requestId, resultType);
		});
	}
	private void waitForCompletion(String requestId) {
		int attempts = 0;
		while (attempts < 10) {
			BlogPostResult post = (BlogPostResult)lilysAiClient.getSummaryResult(requestId, "blogPost").block();
			post.get

			if ("done".equals(status.getStatus())) {
				return;
			}
			if ("error".equals(status.getStatus())) {
				throw new SummaryProcessingException("Processing failed with status: error");
			}

			try {
				Thread.sleep(POLLING_INTERVAL.toMillis());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new SummaryProcessingException("Processing was interrupted", e);
			}
			attempts++;
		}
		throw new SummaryProcessingException("Processing timed out after " + TIMEOUT.toMinutes() + " minutes");
	}



}
