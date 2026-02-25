package com.moda.moda_api.crawling.infrastructure.crawl;

import com.moda.moda_api.crawling.domain.model.CrawledContent;

public interface CrawlingStrategy {
	boolean supports(String url);

	CrawledContent crawl(String url);
}
