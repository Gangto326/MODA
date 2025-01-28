package com.moda.moda_api.summary.presentation;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.summary.application.LilysSummaryService;
import com.moda.moda_api.summary.application.dto.SummaryResponse;
import com.moda.moda_api.summary.domain.CrawlingService;
import com.moda.moda_api.summary.domain.model.CrawledContent;
import com.moda.moda_api.summary.domain.model.Url;

@RestController
@RequestMapping("/api/summary")
public class UrlController {
	private final LilysSummaryService lilysSummaryService;
	private final CrawlingService crawlingService;

	public UrlController(LilysSummaryService lilysSummaryService,
		CrawlingService crawlingService) {
		this.lilysSummaryService = lilysSummaryService;
		this.crawlingService = crawlingService;
	}

	@PostMapping("/lilys/process")
	public CompletableFuture<ResponseEntity<SummaryResponse>> processWithLilys(
		@RequestBody UrlRequest request
	) {
		return lilysSummaryService.summarize(new Url(request.getUrl()), request.getResultType())
			.thenApply(summary -> ResponseEntity.ok(new SummaryResponse(summary.toString())));
	}

	@PostMapping
	public ResponseEntity<CrawledContent> crawl(@RequestBody CrawlRequest request) throws Exception {
		Url url = new Url(request.getUrl());
		CrawledContent content = crawlingService.crawl(url);
		return ResponseEntity.ok(content);
	}
}