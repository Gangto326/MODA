package com.moda.moda_api.summary.application;

import java.time.Duration;
import java.util.ArrayList;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class LilysSummaryService {
	private final LilysAiClient lilysAiClient;
	private final SummaryMapper summaryMapper;
	private final PostRepository postRepository;
	private final SummaryRepository summaryRepository;


	@Transactional
	public CompletableFuture<List<Summary>> summarize(String url) {
		return CompletableFuture.supplyAsync(() -> {
			List<String> resultTypes = getResultTypes(url); //0. 만약 url에 youtube.com이 없으면 timeStamp가 안찍혀야한다.

			log.info("requestId 받기전");
			// 1. requestId를 요청 받는다.
			String requestId = lilysAiClient.getRequestId(url).block().getRequestId();
			log.info("requestId : {}", requestId);

			// 2. 결과가 완성이 될때까지 기다린다.
			waitForCompletion(requestId);
			log.info("받을 준비가 완료되었습니다. ");

			// 3. 모든 타입의 결과를 가져오기
			List<Object> results = resultTypes.stream()
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

			// 5. Post 먼저 저장 -> 원본!!
			Post post = summaries.get(0).getPost();
			postRepository.save(post);

			// 6. Summaries 저장 -> 원본에서 파생되는 요약 5개 저장.
			return summaryRepository.saveAll(summaries);
		});
	}

	private void waitForCompletion(String requestId) {
		int attempts = 0;
		while (attempts < 10) {
			BlogPostResult result = (BlogPostResult)lilysAiClient.getSummaryResult(requestId, "blogPost")
				.block();

			String status = result.getStatus();

			if (status != null && "done".equals(status)) {
				return;
			}
			try {
				log.info("요약이 완성이 될 때까지 기다려야 합니다.. ");
				Thread.sleep(Duration.ofSeconds(30).toMillis());

				attempts++;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new SummaryProcessingException("Processing was interrupted", e);
			}
		}
		throw new SummaryProcessingException("Processing timed out after " + (10 * 30) + " seconds");
	}
	private List<String> getResultTypes(String url) {
		List<String> types = new ArrayList<>(Arrays.asList(
			"blogPost", "summaryNote", "shortSummary"
		));

		if (url.contains("youtube.com")) {
			types.add("timestamp");
			types.add("rawScript");
		}
		return types;
	}
}