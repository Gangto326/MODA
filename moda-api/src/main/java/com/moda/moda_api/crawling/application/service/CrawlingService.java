package com.moda.moda_api.crawling.application.service;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.springframework.stereotype.Service;

import com.moda.moda_api.crawling.infrastructure.crawl.AbstractExtractor;
import com.moda.moda_api.crawling.domain.model.CrawledContent;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrawlingService {
	private final AbstractExtractor extractor;

	// url를 통한 크롤링
	public CrawledContent crawlByUrl(String url) throws TimeoutException, NoSuchElementException {
		CrawledContent crawledContent = extractor.extract(url);
		return crawledContent;
	}

}
