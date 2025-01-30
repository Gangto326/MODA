package com.moda.moda_api.summary.application;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.summary.domain.model.Post;
import com.moda.moda_api.summary.domain.model.Summary;
import com.moda.moda_api.summary.domain.repository.PostRepository;
import com.moda.moda_api.summary.domain.repository.SummaryRepository;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.summary.infrastructure.dto.summaryResult.BlogPostResult;
import com.moda.moda_api.summary.infrastructure.mapper.SummaryMapper;
import com.moda.moda_api.util.exception.SummaryProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class LilysSummaryService {
	private final LilysAiClient lilysAiClient;
	private final SummaryMapper summaryMapper;
	private final PostRepository postRepository;
	private final SummaryRepository summaryRepository;

	private static final List<String> RESULT_TYPES = Arrays.asList(
		"blogPost", "summaryNote", "rawScript", "shortSummary", "timestamp"
	);

	@Transactional
	public CompletableFuture<List<Summary>> summarize(String url) {
		return CompletableFuture.supplyAsync(() -> {
			log.info("requestId 받기전");
			String requestId = lilysAiClient.getRequestId(url).block().getRequestId(); // requestId를 요청 받는다.
			log.info("requestId : {}", requestId);

			waitForCompletion(requestId); // 결과가 완성이 될때까지 기다린다.
			log.info("받을 준비가 완료되었습니다. ");

			// 3. 모든 타입의 결과를 가져오기
			List<Object> results = RESULT_TYPES.stream()
				.map(type -> lilysAiClient.getSummaryResult(requestId, type)
					.doOnError(e -> log.error("Error getting {} result: {}", type, e.getMessage()))
					.block())
				.filter(result -> result != null)
				.collect(Collectors.toList());

			// 4. Post와 Summary 엔티티 생성 및 저장
			List<Summary> summaries = summaryMapper.createSummaries(
				requestId,
				url,
				results.toArray()
			);

			// 5. Post 먼저 저장
			Post post = summaries.get(0).getPost();
			postRepository.save(post);

			// 6. Summaries 저장
			return summaryRepository.saveAll(summaries);
		});
	}

	@Transactional
	public CompletableFuture<List<Summary>> test(String requestId) {
		return CompletableFuture.supplyAsync(() -> {

			List<Object> results = RESULT_TYPES.stream()
				.map(type -> lilysAiClient.getSummaryResult(requestId, type)
					.doOnError(e -> log.error("Error getting {} result: {}", type, e.getMessage()))
					.block())
				.filter(result -> result != null)
				.collect(Collectors.toList());

			// 4. Post와 Summary 엔티티 생성 및 저장
			List<Summary> summaries = summaryMapper.createSummaries(
				requestId,
				"https:",
				results.toArray()
			);

			// 5. Post 먼저 저장
			Post post = summaries.get(0).getPost();
			postRepository.save(post);

			// 6. Summaries 저장
			return summaryRepository.saveAll(summaries);
		});
	}

	private void waitForCompletion(String requestId) {
		int attempts = 0;
		while (attempts < 10) {
			BlogPostResult result = (BlogPostResult)lilysAiClient.getSummaryResult(requestId, RESULT_TYPES.get(0))
				.block();

			String status = result.getStatus();

			if (status != null && "done".equals(status)) {
				return;
			}

			try {
				Thread.sleep(Duration.ofSeconds(50).toMillis());
				log.info("요약이 완성이 될 때까지 기다려야 합니다.. ");
				attempts++;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new SummaryProcessingException("Processing was interrupted", e);
			}
		}
		throw new SummaryProcessingException("Processing timed out after " + (10 * 30) + " seconds");
	}

}