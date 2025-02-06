package com.moda.moda_api.crawling.application.service;

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
	public CrawledContent crawlByUrl(Url url) throws Exception {
		return extractor.extract(url.getValue());
	}

	// KeyWord로 검색후 구글 페이지 크롤링
	// 반환 type은 url들

}