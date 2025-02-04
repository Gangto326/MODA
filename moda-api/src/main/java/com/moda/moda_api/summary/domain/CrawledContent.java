package com.moda.moda_api.summary.domain.crawler;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrawledContent {
	private String url;
	private String title;
	private List<ContentItem> contentItems;
	private LocalDateTime crawledAt;
	private CrawledContentType crawledContentType;
}