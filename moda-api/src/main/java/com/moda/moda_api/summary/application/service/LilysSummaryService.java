package com.moda.moda_api.summary.application.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.common.exception.UnprocessableContentException;
import com.moda.moda_api.summary.application.dto.SummaryResultDto;
import com.moda.moda_api.summary.exception.SummaryProcessingException;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.infrastructure.api.YoutubeApiClient;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
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
	private static final int MAX_ATTEMPTS = 100;
	private static final long POLLING_INTERVAL_SECONDS = 15;
	private final YoutubeApiClient youtubeApiClient;
	private final Executor youtubeExecutor;


	@Transactional
	public CompletableFuture<SummaryResultDto> summarize(String url, String userId) {
		return lilysWebClient.getRequestId(url)
			.thenCompose(requestId ->
				waitForCompletion(requestId.getRequestId())
					.thenApply(status -> requestId)
			)
			.thenCompose(requestId ->
				lilysWebClient.getSummaryResults(requestId.getRequestId(), url)
			)
			.thenCompose(lilysSummary -> {
				CompletableFuture<AIAnalysisResponseDTO> aiAnalysisFuture =
					pythonAnalysisService.youtubeAnalyze(lilysSummary.getContents())
						.exceptionally(e -> {
							throw new UnprocessableContentException(
								userId,
								"해당 영상은 요약 할 수 없는 컨텐츠입니다. 다른 영상을 시도해 주세요"
							);
						});

				CompletableFuture<YoutubeAPIResponseDTO> youtubeApiFuture =
					youtubeApiClient.getVideoData(lilysSummary.getThumbnailUrl());

				return CompletableFuture.allOf(aiAnalysisFuture, youtubeApiFuture)
					.thenApply(v -> {
						AIAnalysisResponseDTO aiAnalysis = aiAnalysisFuture.join();
						YoutubeAPIResponseDTO youtubeAPI = youtubeApiFuture.join();
						String[] keywords = getKeyWords(aiAnalysis, youtubeAPI);
						String[] subContents = getSubContents(lilysSummary, youtubeAPI);

						return SummaryResultDto.builder()
							.typeId(lilysSummary.getTypeId())
							.title(youtubeAPI.getTitle())
							.content(aiAnalysis.getContent())
							.thumbnailContent(youtubeAPI.getChannelTitle())
							.embeddingVector(aiAnalysis.getEmbeddingVector())
							.categoryId(aiAnalysis.getCategoryId())
							.thumbnailUrl(lilysSummary.getThumbnailUrl())
							.keywords(keywords)
							.subContent(subContents)
							.build();
					});
			});
	}

	private String[] getKeyWords(AIAnalysisResponseDTO aiAnalysis, YoutubeAPIResponseDTO youtubeAPI) {
		return Stream.of(
				new String[] {youtubeAPI.getChannelTitle()},
				aiAnalysis.getKeywords(),
				youtubeAPI.getTags()
			)
			.flatMap(Stream::of)
			.toArray(String[]::new);
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
					"Processing timed out after " + (MAX_ATTEMPTS * POLLING_INTERVAL_SECONDS) + " seconds")
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
						CompletableFuture.delayedExecutor(15, TimeUnit.SECONDS, youtubeExecutor)
					).thenCompose(future -> future);
				}
				return CompletableFuture.failedFuture(
					new SummaryProcessingException("Unexpected status: " + status)
				);
			});
	}
}
