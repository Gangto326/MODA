package com.moda.moda_api.summary.domain.service;

import com.moda.moda_api.summary.domain.crawler.ContentType;
import com.moda.moda_api.summary.domain.crawler.CrawledContent;

public interface CrawlingService {
	CrawledContent crawl(String url) throws Exception;
}