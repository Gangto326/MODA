package com.moda.moda_api.summary.infrastructure.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.moda.moda_api.common.infrastructure.ImageStorageService;
import com.moda.moda_api.summary.domain.ContentItem;
import com.moda.moda_api.summary.domain.ContentItemType;
import com.moda.moda_api.summary.domain.CrawledContent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AbstractExtractor {
	private final WebDriver driver;
	private final PlatformExtractorFactory extractorFactory;
	private final ImageStorageService imageStorageService;

	public CrawledContent extract(String url) throws Exception {
		try {
			ExtractorConfig config = extractorFactory.getConfig(url);
			driver.get(url);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

			// iframe 처리
			if (config.isRequiresFrame()) {
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(config.getFrameId()));
			}

			return CrawledContent.builder()
				.url(url)
				.crawledContentType(config.getCrawledContentType())
				.title(extractTitle())
				.contentItems(extractContent(wait, config))
				.build();

		} catch (Exception e) {
			throw new Exception("Failed to crawl content: " + e.getMessage());
		} finally {
			if (driver.switchTo() != null) {
				driver.switchTo().defaultContent();
			}
		}
	}

	public List<ContentItem> extractContent(WebDriverWait wait, ExtractorConfig config) {
		List<CompletableFuture<ContentItem>> futures = new ArrayList<>();

		try {
			WebDriverWait contentWait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement contentElement = contentWait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(config.getContentSelector())
			));

			contentWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath(".//p[not(ancestor::p)]|.//img")
			));

			List<WebElement> elements = contentElement.findElements(By.xpath(".//p[not(ancestor::p)]|.//img"));

			for (WebElement element : elements) {
				try {
					if (element.getTagName().equals("img")) {
						futures.add(CompletableFuture.supplyAsync(() -> {
							try {
								String src = element.getAttribute("src");
								if (src != null && !src.isEmpty() && isValidImageUrl(src)) {
									String savedImageUrl = imageStorageService.uploadImage(src);
									return new ContentItem(savedImageUrl, ContentItemType.IMAGE);
								}
							} catch (Exception e) {
								log.error("Failed to process image", e);
							}
							return null;
						}));
					} else {
						String text = element.getText().trim();
						if (!text.isEmpty()) {
							futures.add(CompletableFuture.completedFuture(
								new ContentItem(text, ContentItemType.TEXT)
							));
						}
					}
				} catch (StaleElementReferenceException e) {
					continue;
				}
			}

			return futures.stream()
				.map(CompletableFuture::join)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("Failed to extract content", e);
			return new ArrayList<>();
		}
	}

	private String extractTitle() {
		return driver.getTitle();
	}

	private boolean isValidImageUrl(String url) {
		return url.matches(".*\\.(jpg|jpeg|png|gif|webp)($|\\?.*)") &&
			!url.contains("favicon") &&
			!url.contains("logo") &&
			url.length() > 10;
	}
}