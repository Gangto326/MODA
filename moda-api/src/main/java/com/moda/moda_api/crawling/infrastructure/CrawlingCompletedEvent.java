package com.moda.moda_api.crawling.infrastructure;

import java.time.LocalDateTime;
import java.util.UUID;

import com.moda.moda_api.crawling.domain.model.CrawledContent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CrawlingCompletedEvent {
	private final CrawledContent crawledContent;

	public static CrawlingCompletedEvent of(CrawledContent crawledContent) {
		return new CrawlingCompletedEvent(crawledContent);
	}

}