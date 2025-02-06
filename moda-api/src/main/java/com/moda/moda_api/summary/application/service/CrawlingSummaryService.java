package com.moda.moda_api.summary.application.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moda.moda_api.crawling.application.service.CrawlingService;
import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.summary.application.dto.SummaryResultDto;
import com.moda.moda_api.summary.domain.ContentItem;
import com.moda.moda_api.summary.domain.ContentItemType;
import com.moda.moda_api.summary.infrastructure.api.PythonAnalysisService;
import com.moda.moda_api.summary.infrastructure.dto.PythonAnalysisDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CrawlingSummaryService {
	private final PythonAnalysisService pythonAnalysisService;
	private final CrawlingService crawlingService;

	public CompletableFuture<SummaryResultDto> summarize(String url) throws Exception {

		CrawledContent crawledContent = crawlingService.crawlByUrl(url);

		CompletableFuture<PythonAnalysisDto> pythonAnalysisDtoCompletableFuture = pythonAnalysisService.analyzeSummary(
			crawledContent.getContentItems());

		return pythonAnalysisDtoCompletableFuture.thenApply(pythonAnalysisDto -> {
			// JsonNode 초기화
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode emptyJsonNode = objectMapper.createObjectNode();

			// 첫 번째 이미지 URL 가져오기
			String thumbnailUrl = getFirstImageUrl(crawledContent.getContentItems());

			// SummaryResultDto 생성
			return SummaryResultDto.builder()
				.typeId(crawledContent.getUrl().getCardContentType().getTypeId())
				.title(crawledContent.getTitle())
				.content(emptyJsonNode.toString())
				.keyword(pythonAnalysisDto.getKeywords())  // Python 분석에서 받은 키워드
				.thumbnailContent(pythonAnalysisDto.getThumbnailContent())  // Python 분석에서 받은 thumbnail content
				.thumbnailUrl(thumbnailUrl)  // 첫 번째 이미지 URL
				.embeddingVector(pythonAnalysisDto.getEmbeddingVector())  // Python 분석에서 받은 EmbeddingVector
				.categoryId(pythonAnalysisDto.getCategoryId())  // Python 분석에서 받은 CategoryId
				.build();
		});

	}

	public String getFirstImageUrl(List<ContentItem> contentItems) {
		// 첫 번째 이미지 찾기
		Optional<ContentItem> firstImage = contentItems.stream()
			.filter(item -> item.getType() == ContentItemType.IMAGE)  // 이미지 타입만 필터링
			.findFirst();  // 첫 번째 이미지만 찾기

		// 이미지가 있으면 해당 content의 URL을 반환, 없으면 빈 문자열 반환
		return firstImage.map(ContentItem::getContent).orElse("");
	}

}
