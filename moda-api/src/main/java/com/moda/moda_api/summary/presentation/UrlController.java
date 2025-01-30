package com.moda.moda_api.summary.presentation;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.summary.application.LilysSummaryService;
import com.moda.moda_api.summary.domain.CrawlingService;
import com.moda.moda_api.summary.domain.model.CrawledContent;
import com.moda.moda_api.summary.domain.model.Url;
import com.moda.moda_api.summary.presentation.dto.CrawlRequest;
import com.moda.moda_api.summary.presentation.dto.SummaryResponse;
import com.moda.moda_api.summary.presentation.dto.UrlRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/summary")
@Slf4j
public class UrlController {
	private final LilysSummaryService lilysSummaryService;
	private final CrawlingService crawlingService;

	public UrlController(LilysSummaryService lilysSummaryService,
		CrawlingService crawlingService) {
		this.lilysSummaryService = lilysSummaryService;
		this.crawlingService = crawlingService;
	}

	@PostMapping("/lilys/process")
	public CompletableFuture<ResponseEntity<SummaryResponse>> processWithLilys( @RequestBody UrlRequest request) {
		log.info("url 받고나서");
		return lilysSummaryService.summarize(request.getUrl())
			.thenApply(summary -> ResponseEntity.ok(new SummaryResponse(summary.toString())));
	}

	@PostMapping("/crawl")
	public ResponseEntity<CrawledContent> crawl(@RequestBody CrawlRequest request) throws Exception {
		Url url = new Url(request.getUrl());
		CrawledContent content = crawlingService.crawl(url);
		return ResponseEntity.ok(content);
	}

}