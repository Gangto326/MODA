package com.moda.moda_api.summary.presentation;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.summary.application.LilysSummaryService;
import com.moda.moda_api.summary.domain.crawler.ContentType;
import com.moda.moda_api.summary.domain.crawler.CrawledContent;
import com.moda.moda_api.summary.domain.service.CrawlingService;
import com.moda.moda_api.summary.domain.service.UrlAnalyzerService;
import com.moda.moda_api.summary.presentation.dto.CrawlRequest;
import com.moda.moda_api.summary.presentation.dto.CrawlingResponse;
import com.moda.moda_api.summary.presentation.dto.SummaryResponse;
import com.moda.moda_api.summary.presentation.dto.UrlRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/summary")
@Slf4j
@AllArgsConstructor
public class UrlController {
	private final LilysSummaryService lilysSummaryService;
	private final CrawlingService crawlingService;
	private final UrlAnalyzerService urlAnalyzerService;

	@PostMapping("/lilys/process")
	public CompletableFuture<ResponseEntity<SummaryResponse>> processWithLilys( @RequestBody UrlRequest request) {
		log.info("url 받고나서");
		return lilysSummaryService.summarize(request.getUrl())
			.thenApply(summary -> ResponseEntity.ok(new SummaryResponse(summary.toString())));
	}
	@PostMapping("/crawl")
	public ResponseEntity<CrawlingResponse> crawl(@RequestBody CrawlRequest request) {
		try {
			ContentType contentType = urlAnalyzerService.analyzeUrlType(request.getUrl());
			CrawledContent content = crawlingService.crawl(request.getUrl());

			return ResponseEntity.ok(new CrawlingResponse(
				content.getTitle(),
				content.getContent(),
				content.getImageUrls(),
				content.getCrawledAt(),
				"Crawling completed successfully"
			));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
				.body(new CrawlingResponse(null, null, null, null,
					"Unsupported URL: " + e.getMessage()));
		}
	}

}