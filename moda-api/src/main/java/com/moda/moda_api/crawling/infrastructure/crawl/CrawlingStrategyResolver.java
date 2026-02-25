package com.moda.moda_api.crawling.infrastructure.crawl;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CrawlingStrategyResolver {
	private final List<CrawlingStrategy> strategies;

	public CrawlingStrategyResolver(
		JsoupCrawler jsoupCrawler,
		VelogGraphQLCrawler velogGraphQLCrawler,
		SeleniumCrawler seleniumCrawler
	) {
		// 순서 중요: 구체적인 전략이 먼저, Selenium은 마지막 (폴백)
		this.strategies = List.of(
			velogGraphQLCrawler,
			jsoupCrawler,
			seleniumCrawler
		);
	}

	public CrawlingStrategy resolve(String url) {
		for (CrawlingStrategy strategy : strategies) {
			if (strategy.supports(url)) {
				log.info("[Resolver] URL '{}' → {}", url, strategy.getClass().getSimpleName());
				return strategy;
			}
		}
		// SeleniumCrawler.supports()가 항상 true이므로 여기 도달 불가
		throw new RuntimeException("No crawling strategy found for: " + url);
	}
}
