package com.moda.moda_api.crawling.application.service;

import org.springframework.stereotype.Service;

import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.crawling.infrastructure.crawl.CrawlingStrategyResolver;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrawlingService {
	private final CrawlingStrategyResolver strategyResolver;

	public CrawledContent crawlByUrl(String url) {
		return strategyResolver.resolve(url).crawl(url);
	}
}
