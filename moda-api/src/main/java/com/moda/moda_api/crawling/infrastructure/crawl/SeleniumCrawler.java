package com.moda.moda_api.crawling.infrastructure.crawl;

import org.springframework.stereotype.Component;

import com.moda.moda_api.crawling.domain.model.CrawledContent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SeleniumCrawler implements CrawlingStrategy {
	private final AbstractExtractor abstractExtractor;
	private final Timer seleniumTimer;
	private final Counter seleniumCounter;

	public SeleniumCrawler(AbstractExtractor abstractExtractor, MeterRegistry registry) {
		this.abstractExtractor = abstractExtractor;
		this.seleniumTimer = Timer.builder("moda.crawling.duration")
			.tag("strategy", "selenium")
			.description("Selenium crawling duration")
			.register(registry);
		this.seleniumCounter = Counter.builder("moda.crawling.strategy")
			.tag("strategy", "selenium")
			.description("Selenium crawling count")
			.register(registry);
	}

	@Override
	public boolean supports(String url) {
		// 폴백: 다른 전략이 지원하지 않는 모든 URL
		return true;
	}

	@Override
	public CrawledContent crawl(String url) {
		return seleniumTimer.record(() -> {
			seleniumCounter.increment();
			log.info("[Selenium] Crawling CSR/unsupported site: {}", url);
			return abstractExtractor.extract(url);
		});
	}
}
