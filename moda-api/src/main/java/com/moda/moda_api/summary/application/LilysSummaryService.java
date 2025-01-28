package com.moda.moda_api.summary.application;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.domain.model.Summary;
import com.moda.moda_api.summary.domain.model.Url;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.infrastructure.dto.LilysAiResponse;
import com.moda.moda_api.summary.infrastructure.dto.SummaryResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LilysSummaryService {
    private final LilysAiClient lilysAiClient;

    public LilysSummaryService(LilysAiClient lilysAiClient){
        this.lilysAiClient = lilysAiClient;
    }

    public CompletableFuture<Summary> summarize(Url url, String resultType) {
        return CompletableFuture.supplyAsync(() -> {
            LilysAiResponse initialResponse = lilysAiClient.callAi(url.getValue());
            String requestId = initialResponse.getRequestId();
            return getSummaryAsync(requestId, resultType);
        });
    }

    private Summary getSummaryAsync(String requestId, String resultType) {
        int maxRetries = 10;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                SummaryResult result = lilysAiClient.getSummaryResult(requestId, resultType);

                if (result.getStatus() != null) {
                    if ("pending".equalsIgnoreCase(result.getStatus())) {
                        log.info("Summary status: pending, waiting...");
                        TimeUnit.SECONDS.sleep(10);
                        retryCount++;
                        continue;
                    }

                    if ("done".equalsIgnoreCase(result.getStatus())) {
                        Summary summary = new Summary(result.getData().toString(), resultType, "done");
                        return summary;
                    }

                    log.warn("Unexpected summary status: {}", result.getStatus());
                    break;
                }

                log.warn("Received null status, retrying...");
                retryCount++;
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for summary completion", ie);
            } catch (Exception e) {
                log.warn("Error while fetching summary: {}", e.getMessage());
                retryCount++;

                if (retryCount >= maxRetries) {
                    break;
                }
            }
        }

        throw new RuntimeException("Failed to retrieve summary after multiple attempts");
    }
} 