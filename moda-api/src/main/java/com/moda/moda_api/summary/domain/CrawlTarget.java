package com.moda.moda_api.summary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrawlTarget {
	private String domainPattern;
	private CrawledContentType crawledContentType;
	private String contentSelector;  // 각 사이트별 본문 선택자

}