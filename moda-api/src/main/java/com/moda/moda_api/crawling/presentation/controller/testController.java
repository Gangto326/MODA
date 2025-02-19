package com.moda.moda_api.crawling.presentation.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.card.presentation.request.CardRequest;
import com.moda.moda_api.crawling.application.service.CrawlingService;
import com.moda.moda_api.crawling.domain.model.CrawledContent;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class testController {

	private final CrawlingService crawlerService;

	@PostMapping("/crawl")
	public CompletableFuture<ResponseEntity<CrawledContent>> crawlUrl(@RequestBody @Valid CardRequest cardRequest) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				CrawledContent content = crawlerService.crawlByUrl(cardRequest.getUrl());
				System.out.println(content.getExtractedContent().getText());
				return ResponseEntity.ok(content);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		});
	}


}
