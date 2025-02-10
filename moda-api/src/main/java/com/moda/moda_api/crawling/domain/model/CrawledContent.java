package com.moda.moda_api.crawling.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrawledContent {
	private Url url;
	private String title;
	private ExtractedContent extractedContent;
	private LocalDateTime crawledAt;
	private UrlDomainType urlDomainType;
}