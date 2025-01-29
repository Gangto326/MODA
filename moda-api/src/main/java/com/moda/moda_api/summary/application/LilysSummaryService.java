package com.moda.moda_api.summary.application;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.domain.model.Summary;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.infrastructure.dto.LilysAiResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LilysSummaryService {
    private final LilysAiClient lilysAiClient;

    public LilysSummaryService(LilysAiClient lilysAiClient){
        this.lilysAiClient = lilysAiClient;
    }


    public CompletableFuture<Summary> summarize(String url) {
        return CompletableFuture.supplyAsync(() -> {
            Mono<LilysAiResponse> requestId = lilysAiClient.getRequestId(url); // requestId를 요청 받는다.

        });
    }
} 