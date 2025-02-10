package com.moda.moda_api.summary.application.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.summary.application.dto.SummaryResultDto;
import com.moda.moda_api.summary.exception.SummaryProcessingException;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.infrastructure.api.YoutubeApiClient;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.summary.infrastructure.dto.LilysRequestIdResponse;
import com.moda.moda_api.summary.infrastructure.dto.LilysSummary;
import com.moda.moda_api.summary.infrastructure.dto.YoutubeAPIResponseDTO;

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
	private final YoutubeApiClient youtubeApiClient;


	@Transactional
	public SummaryResultDto summarize(String url) {
		LilysRequestIdResponse requestId = lilysWebClient.getRequestId(url);
		waitForCompletion(requestId.getRequestId());
		LilysSummary lilysSummary = lilysWebClient.getSummaryResults(requestId.getRequestId(), url);
		AIAnalysisResponseDTO aiAnalysisResponseDTO = pythonAnalysisService.youtubeAnalyze(
			lilysSummary.getContents());
		// 여기서 만들어지고
		YoutubeAPIResponseDTO youtubeAPIResponseDTO = youtubeApiClient.getVideoData(lilysSummary.getThumbnailUrl());

		String[] keyWords = Stream.concat(Arrays.stream(youtubeAPIResponseDTO.getTags()),
			Arrays.stream(aiAnalysisResponseDTO.getKeywords())).toArray(String[]::new);

		String[] subContents = getSubContents(lilysSummary, youtubeAPIResponseDTO);

		return SummaryResultDto.builder()
			.typeId(lilysSummary.getTypeId())
			.title(lilysSummary.getTitle())
			.content(aiAnalysisResponseDTO.getContent())
			.thumbnailContent(youtubeAPIResponseDTO.getChannelTitle())
			.embeddingVector(aiAnalysisResponseDTO.getEmbeddingVector())
			.categoryId(aiAnalysisResponseDTO.getCategoryId())
			.keywords(keyWords)
			.subContent(subContents)
			.build();
	}

	private String[] getSubContents(LilysSummary lilysSummary, YoutubeAPIResponseDTO youtubeAPIResponseDTO) {
		String[] timeStamps = lilysSummary.getTimeStamp();
		String[] subContents = new String[timeStamps.length + 2];

		subContents[0] = youtubeAPIResponseDTO.getDescription();
		subContents[1] = youtubeAPIResponseDTO.getChannelThumbnailUrl();
		System.arraycopy(timeStamps, 0, subContents, 2, timeStamps.length);
		return subContents;
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
