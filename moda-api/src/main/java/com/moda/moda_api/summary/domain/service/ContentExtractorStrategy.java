package com.moda.moda_api.summary.domain.service;

import com.moda.moda_api.summary.domain.crawler.CrawledContent;

public interface ContentExtractorStrategy {
	boolean supports(String url);
	CrawledContent extract(String url) throws Exception;
}