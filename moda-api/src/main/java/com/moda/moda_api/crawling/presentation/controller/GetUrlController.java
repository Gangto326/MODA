package com.moda.moda_api.crawling.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moda.moda_api.crawling.application.service.KeywordSearchService;
import com.moda.moda_api.crawling.domain.model.Url;
import com.moda.moda_api.crawling.presentation.dto.SearchRequest;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/test")
@AllArgsConstructor
public class GetUrlController {
	private final KeywordSearchService keywordSearchService;

	@PostMapping
	public List<Url> search(
		@RequestBody SearchRequest searchRequest) throws Exception {
		return keywordSearchService.crawlByKeyWord(searchRequest.getKeyword());
	}
}
