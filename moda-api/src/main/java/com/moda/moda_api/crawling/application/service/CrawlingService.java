package com.moda.moda_api.crawling.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moda.moda_api.crawling.infrastructure.crawl.AbstractExtractor;
import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.crawling.domain.model.Url;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrawlingService {
	private final AbstractExtractor extractor;

	// url를 통한 크롤링
	public CrawledContent crawlByUrl(String url) throws Exception {
		return extractor.extract(url);
	}

}