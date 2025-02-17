package com.moda.moda_api.summary.application.service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.category.domain.CategoryId;
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
	private static final Duration POLLING_INTERVAL = Duration.ofSeconds(200);
	private final YoutubeApiClient youtubeApiClient;

	@Transactional
	public CompletableFuture<SummaryResultDto> summarize(String url) {
		return CompletableFuture.supplyAsync(() -> lilysWebClient.getRequestId(url))
			.thenCompose(requestId -> {
				// RequestId를 받은 후 waitForCompletion 실행하고 완료될 때까지 대기
				return waitForCompletion(requestId.getRequestId())
					.thenApply(status -> requestId);
			})
			.thenCompose(requestId -> {
				// waitForCompletion이 완전히 완료된 후에만 getSummaryResults 실행
				return CompletableFuture.supplyAsync(() ->
					lilysWebClient.getSummaryResults(requestId.getRequestId(), url)
				);
			})
			.thenCompose(lilysSummary -> {

				// 서버 전용
				// getSummaryResults 완료 후 병렬로 실행 가능한 작업들
				// CompletableFuture<AIAnalysisResponseDTO> aiAnalysisFuture =
				// 	CompletableFuture.supplyAsync(() ->
				// 		pythonAnalysisService.youtubeAnalyze(lilysSummary.getContents())
				// 	);

				CompletableFuture<AIAnalysisResponseDTO> aiAnalysisFuture = CompletableFuture.completedFuture(
					AIAnalysisResponseDTO.builder()
						.categoryId(new CategoryId(2L))  // null 허용
						.keywords(new String[] {"박종원_02_16~02_17_Test"})
						.thumbnailContent("박종원_02_16~02_17_Test")
						.content(lilysSummary.getContents().stream()
							.map(content -> content.getTitle() + ": " + content.getContent())
							.collect(Collectors.joining("\n")))
						.embeddingVector(null)
						.build()
				);


				CompletableFuture<YoutubeAPIResponseDTO> youtubeApiFuture =
					CompletableFuture.supplyAsync(() ->
						youtubeApiClient.getVideoData(lilysSummary.getThumbnailUrl())
					);

				// 두 작업이 모두 완료될 때까지 대기 후 결과 조합
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
