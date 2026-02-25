package com.moda.moda_api.summary.application.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.moda.moda_api.common.exception.ContentExtractionException;
import com.moda.moda_api.crawling.domain.service.CardContentTypeResolver;
import com.moda.moda_api.summary.application.dto.SummaryResultDto;
import com.moda.moda_api.summary.infrastructure.api.PythonAiClient;
import com.moda.moda_api.summary.infrastructure.dto.CrawlResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlingSummaryService {
	private final PythonAiClient pythonAiClient;

	public CompletableFuture<SummaryResultDto> summarize(String url, String userId) {
		log.info("크롤링+분석 요청 → Python: {}", url);

		return pythonAiClient.crawlAndAnalyze(url)
			.exceptionally(throwable -> {
				throw new ContentExtractionException(userId, "요약 할 수 없는 사이트입니다." + url, throwable);
			})
			.thenApply(response -> {
				log.info("Python 응답 수신 — title='{}', domain={}", response.getTitle(), response.getDomainType());

				String thumbnailUrl = getFirstImageUrl(response.getImages());
				int typeId = CardContentTypeResolver.resolve(url).getTypeId();

				return SummaryResultDto.builder()
					.typeId(typeId)
					.title(response.getTitle())
					.content(response.getContent())
					.keywords(response.getKeywords())
					.thumbnailContent(response.getThumbnailContent())
					.thumbnailUrl(thumbnailUrl != null ? thumbnailUrl : response.getThumbnailUrl())
					.embeddingVector(response.getEmbeddingVector())
					.categoryId(response.getCategoryId())
					.subContent(response.getImages())
					.build();
			});
	}

	private String getFirstImageUrl(String[] images) {
		if (images == null || images.length == 0) {
			return null;
		}

		for (String imageUrl : images) {
			if (imageUrl != null && !imageUrl.trim().isEmpty()) {
				return imageUrl;
			}
		}
		return null;
	}

}
