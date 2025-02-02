package com.moda.moda_api.summary.domain.crawler;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrawledContent {
	private String url;
	private String title;
	private String content;
	private List<String> imageUrls;
	private LocalDateTime crawledAt;
	private CrawledContentType crawledContentType;

}