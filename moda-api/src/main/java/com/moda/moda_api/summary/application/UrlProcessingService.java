package com.moda.moda_api.summary.application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.infrastructure.dto.LilysAiResponse;
import com.moda.moda_api.summary.infrastructure.dto.SummaryResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UrlProcessingService {
	private final LilysAiClient lilysAiClient;

	public UrlProcessingService(LilysAiClient lilysAiClient) {
		this.lilysAiClient = lilysAiClient;
	}


	
	//resultType의 종류 : "shortSummary" | "summaryNote" | "rawScript" | "timestamp" | "blogPost";
	public CompletableFuture<Object> processUrl(String url, String resultType) {
		System.out.println(url);

		return CompletableFuture.supplyAsync(() -> {
			LilysAiResponse initialResponse = lilysAiClient.callAi(url);
			String requestId = initialResponse.getRequestId();
			System.out.println(requestId);
			return getSummaryAsync(requestId, resultType);
		});

	}

	@Async
	public CompletableFuture<Object> getSummaryAsync(String requestId, String resultType) {
		return CompletableFuture.supplyAsync(() -> {
			int maxRetries = 10; // 최대 재시도 횟수
			int retryCount = 0;

			while (retryCount < maxRetries) {
				try {
					// 결과를 가져오기
					SummaryResult result = lilysAiClient.getSummaryResult(requestId, resultType);


					// status가 null이 아닌 경우
					if (result.getStatus() != null) {
						// pending 상태라면 대기
						if ("pending".equalsIgnoreCase(result.getStatus())) {
							log.info("Summary status: pending, waiting...");
							TimeUnit.SECONDS.sleep(10); // 10초 대기
							retryCount++;
							continue; // 다시 반복
						}

						// done 상태라면 결과 반환
						if ("done".equalsIgnoreCase(result.getStatus())) {
							System.out.println(result.getData());
							System.out.println(result.getStatus());
							return result;
						}

						// 다른 상태(error 등)라면 더 이상 재시도 하지 않고 종료
						log.warn("Unexpected summary status: {}", result.getStatus());
						break;
					}

					// status가 null인 경우 재시도
					log.warn("Received null status, retrying...");
					retryCount++;
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Interrupted while waiting for summary completion", ie);
				} catch (Exception e) {
					log.warn("Error while fetching summary: {}", e.getMessage());
					retryCount++;

					// 심각한 오류 발생 시 더 이상 재시도 하지 않음
					if (retryCount >= maxRetries) {
						break;
					}
				}
			}

			// 최대 재시도 횟수 초과 또는 더 이상 진행할 수 없는 상태
			throw new RuntimeException("Failed to retrieve summary after multiple attempts");
		});
	}
}
