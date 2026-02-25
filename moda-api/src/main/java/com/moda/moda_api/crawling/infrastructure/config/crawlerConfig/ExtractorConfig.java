package com.moda.moda_api.crawling.infrastructure.config.crawlerConfig;

import com.moda.moda_api.crawling.domain.model.UrlDomainType;

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
	private String urlSelector;
	private UrlDomainType urlDomainType;
}