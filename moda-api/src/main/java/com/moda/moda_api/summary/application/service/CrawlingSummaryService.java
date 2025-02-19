package com.moda.moda_api.summary.application.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.common.exception.ContentExtractionException;
import com.moda.moda_api.common.exception.UnprocessableContentException;
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

	public CompletableFuture<SummaryResultDto> summarize(String url, String userId) {
		return CompletableFuture.supplyAsync(() -> {
				try {
					System.out.println("크롤링하기 직전");
					return crawlingService.crawlByUrl(url);
				} catch (Exception e) {
					throw new ContentExtractionException(userId, "요약 할 수 없는 사이트입니다." + url, e);
				}
			}).exceptionally(throwable -> {
				if (throwable.getCause() instanceof ContentExtractionException) {
					throw (ContentExtractionException)throwable.getCause();
				}
				throw new ContentExtractionException(userId, "요약 중 오류가 발생했습니다: " + url, throwable);
			})  // 여기서 닫는 괄호가 잘못 배치되어 있었습니다
			.thenCompose(crawledContent -> {

				log.info(crawledContent.getExtractedContent().getText());

				// 2단계: Python 분석과 이미지 URL 가져오기를 병렬로 실행
				CompletableFuture<AIAnalysisResponseDTO> pythonAnalysisFuture =
					CompletableFuture.supplyAsync(() -> {
						try {
							return pythonAnalysisService.articleAnalyze(
								crawledContent.getExtractedContent().getText()
							);
						} catch (WebClientResponseException.InternalServerError e) {
							throw new UnprocessableContentException(
								userId,
								"요약할 수 없는 사이트입니다."
							);
						} catch (Exception e) {

							throw new CompletionException("Python analysis failed", e);
						}
					});

				// CompletableFuture<AIAnalysisResponseDTO> pythonAnalysisFuture = CompletableFuture.completedFuture(
				// 	AIAnalysisResponseDTO.builder()
				// 		.categoryId(new CategoryId(2L))  // null 허용
				// 		.keywords(new String[]{"새로운 test"})
				// 		.thumbnailContent("새로운 test")
				// 		.content(crawledContent.getExtractedContent().getText())
				// 		.embeddingVector(new EmbeddingVector(null))
				// 		.build()
				// );

				CompletableFuture<String> thumbnailUrlFuture =
					CompletableFuture.supplyAsync(() ->
						getFirstImageUrl(crawledContent.getExtractedContent().getImages())
					);

				// 3단계: 두 작업이 모두 완료되면 최종 결과 생성
				return CompletableFuture.allOf(pythonAnalysisFuture, thumbnailUrlFuture)
					.thenApply(ignored -> {
						AIAnalysisResponseDTO pythonAnalysisDto = pythonAnalysisFuture.join();
						String thumbnailUrl = thumbnailUrlFuture.join();

						return SummaryResultDto.builder()
							.typeId(crawledContent.getUrl().getCardContentType().getTypeId())
							.title(crawledContent.getTitle())
							.content(pythonAnalysisDto.getContent())
							.keywords(pythonAnalysisDto.getKeywords())
							.thumbnailContent(pythonAnalysisDto.getThumbnailContent())
							.thumbnailUrl(thumbnailUrl)
							.embeddingVector(pythonAnalysisDto.getEmbeddingVector())
							.categoryId(pythonAnalysisDto.getCategoryId())
							.subContent(crawledContent.getExtractedContent().getImages())
							.build();
					});
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
