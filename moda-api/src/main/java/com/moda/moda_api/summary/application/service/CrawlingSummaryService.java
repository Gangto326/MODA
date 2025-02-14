package com.moda.moda_api.summary.application.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.moda.moda_api.crawling.application.service.CrawlingService;
import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.summary.application.dto.SummaryResultDto;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlingSummaryService {
	private final PythonAnalysisService pythonAnalysisService;
	private final CrawlingService crawlingService;

	public CompletableFuture<SummaryResultDto> summarize(String url) throws Exception {

		CrawledContent crawledContent = crawlingService.crawlByUrl(url);

		log.info(crawledContent.getExtractedContent().getText());

		CompletableFuture<AIAnalysisResponseDTO> pythonAnalysisDtoCompletableFuture
			= pythonAnalysisService.articleAnalyze(crawledContent.getExtractedContent().getText());

		return pythonAnalysisDtoCompletableFuture.thenApply(pythonAnalysisDto -> {
			// 첫 번째 이미지 URL 가져오기
			String thumbnailUrl = getFirstImageUrl(crawledContent.getExtractedContent().getImages());

			System.out.println(pythonAnalysisDto.toString());
			// SummaryResultDto 생성
			return SummaryResultDto.builder()
				.typeId(crawledContent.getUrl().getCardContentType().getTypeId())
				.title(crawledContent.getTitle())
				.content(pythonAnalysisDto.getContent())
				.keywords(pythonAnalysisDto.getKeywords())  // Python 분석에서 받은 키워드
				.thumbnailContent(pythonAnalysisDto.getThumbnailContent())  // Python 분석에서 받은 thumbnail content
				.thumbnailUrl(thumbnailUrl)  // 첫 번째 이미지 URL
				.embeddingVector(pythonAnalysisDto.getEmbeddingVector())  // Python 분석에서 받은 EmbeddingVector
				.categoryId(pythonAnalysisDto.getCategoryId())  // Python 분석에서 받은 CategoryId
				.subContent(crawledContent.getExtractedContent().getImages())
				.build();
		});
	}

	/**
	 * 이미지 URL 배열에서 첫 번째 유효한 이미지 URL을 반환합니다.
	 *
	 * @param images 이미지 URL 배열
	 * @return 첫 번째 유효한 이미지 URL, 유효한 이미지가 없는 경우 null 반환
	 */
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
