package com.moda.moda_api.summary.infrastructure.service;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.domain.crawler.CrawledContent;
import com.moda.moda_api.summary.domain.service.ContentExtractorService;
import com.moda.moda_api.summary.infrastructure.service.platformStartegy.AbstractExtractor;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeleniumContentExtractor implements ContentExtractorService {
	private final WebDriver driver;
	private final AbstractExtractor abstractExtractor;

	public SeleniumContentExtractor(PlatformExtractorFactory extractorFactory) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
		this.driver = new ChromeDriver(options);
		this.abstractExtractor = new AbstractExtractor(driver, extractorFactory);
	}

	@Override
	public String extractContent(String url) {
		try {
			CrawledContent content = abstractExtractor.extract(url);
			return content.getContent();
		} catch (Exception e) {
			log.error("Failed to extract content from url: {}", url, e);
			return "";
		}
	}

	@Override
	public String extractTitle(String url) {
		try {
			CrawledContent content = abstractExtractor.extract(url);
			return content.getTitle();
		} catch (Exception e) {
			log.error("Failed to extract title from url: {}", url, e);
			return "";
		}
	}

	@Override
	public List<String> extractImages(String url) {
		try {
			CrawledContent content = abstractExtractor.extract(url);
			return content.getImageUrls();
		} catch (Exception e) {
			log.error("Failed to extract images from url: {}", url, e);
			return List.of();
		}
	}

	@PreDestroy
	public void cleanup() {
		if (driver != null) {
			driver.quit();
		}
	}
}