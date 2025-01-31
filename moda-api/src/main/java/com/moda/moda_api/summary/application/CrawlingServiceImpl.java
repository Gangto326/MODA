package com.moda.moda_api.summary.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.domain.crawler.CrawledContent;
import com.moda.moda_api.summary.domain.service.ContentExtractorStrategy;
import com.moda.moda_api.summary.domain.service.CrawlingService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {
	private final List<ContentExtractorStrategy> extractorStrategies;

	@Override
	public CrawledContent crawl(String url) throws Exception {
		ContentExtractorStrategy strategy = extractorStrategies.stream()
			.filter(extractor -> extractor.supports(url))
			.findFirst()
			.orElseThrow(() -> new UnsupportedOperationException("Unsupported website: " + url));

		CrawledContent content = strategy.extract(url);
		content.setCrawledAt(LocalDateTime.now());
		return content;
	}

}