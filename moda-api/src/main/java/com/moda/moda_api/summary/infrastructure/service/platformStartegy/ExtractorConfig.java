package com.moda.moda_api.summary.infrastructure.service.platformStartegy;

import com.moda.moda_api.summary.domain.crawler.CrawledContentType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExtractorConfig {
	private String pattern;
	private String contentSelector;
	private String imageSelector;
	private boolean requiresFrame;
	private String frameId;
	private CrawledContentType crawledContentType;
}