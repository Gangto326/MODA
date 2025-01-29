package com.moda.moda_api.summary.domain;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.domain.model.CrawledContent;
import com.moda.moda_api.summary.infrastructure.WebCrawler;

@Service
public class CrawlingService {
	private final WebCrawler webCrawler;

	public CrawlingService(WebCrawler webCrawler) {
		this.webCrawler = webCrawler;
	}

	public CrawledContent crawl(Url url) throws Exception {
		return webCrawler.crawl(url);
	}
}
