package com.moda.moda_api.summary.application.service;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.domain.CrawledContent;
import com.moda.moda_api.summary.infrastructure.service.AbstractExtractor;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrawlingServiceImpl {
	private final AbstractExtractor extractor;

	public CrawledContent crawl(String url) throws Exception {
		return extractor.extract(url);
	}

}