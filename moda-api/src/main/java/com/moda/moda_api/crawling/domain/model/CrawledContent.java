package com.moda.moda_api.crawling.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.moda.moda_api.card.domain.Content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrawledContent {
	private String url;
	private String title;
	private List<Content> contents;
	private LocalDateTime crawledAt;
	private UrlDomainType urlDomainType;
}