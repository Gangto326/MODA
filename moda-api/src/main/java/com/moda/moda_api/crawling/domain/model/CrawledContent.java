package com.moda.moda_api.crawling.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.moda.moda_api.summary.domain.ContentItem;

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
	private List<ContentItem> contentItems;
	private LocalDateTime crawledAt;
	private UrlDomainType urlDomainType;
}