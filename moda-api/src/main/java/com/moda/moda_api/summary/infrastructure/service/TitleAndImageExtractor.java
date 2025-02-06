package com.moda.moda_api.summary.infrastructure.service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.moda.moda_api.common.infrastructure.ImageStorageService;
import com.moda.moda_api.crawling.infrastructure.crawl.AbstractExtractor;
import com.moda.moda_api.crawling.infrastructure.config.crawlerConfig.ExtractorConfig;
import com.moda.moda_api.crawling.infrastructure.config.crawlerConfig.PlatformExtractorFactory;
import com.moda.moda_api.summary.domain.ContentItem;
import com.moda.moda_api.summary.domain.ContentItemType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TitleAndImageExtractor {
	private final WebDriver driver;
	private final AbstractExtractor extractor;
	private final PlatformExtractorFactory extractorFactory;
	private final ImageStorageService imageStorageService;

	public CompletableFuture<String> extractTitleAsync(String url) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				driver.get(url);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

				// 페이지 로드 완료 대기
				wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
				wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("title")));
				wait.until(driver -> !driver.getTitle().isEmpty());

				String title = driver.getTitle();

				if (title.isEmpty()) {
					WebElement ogTitle = wait.until(
						ExpectedConditions.presenceOfElementLocated(
							By.cssSelector("meta[property='og:title']")
						)
					);
					title = ogTitle.getAttribute("content");
				}

				return title;
			} catch (TimeoutException e) {
				log.warn("Timeout while extracting title from URL: {}", url, e);
				return "";
			} catch (Exception e) {
				log.error("Failed to extract title from URL: {}", url, e);
				return "";
			}
		});
	}

	public CompletableFuture<String> extractImageAsync(String url) {
		if (url.contains("youtube.com")) {
			return CompletableFuture.completedFuture(url);
		}

		return CompletableFuture.supplyAsync(() -> {
			ExtractorConfig config = null;  // try 블록 밖에서 선언
			try {
				config = extractorFactory.getConfig(url);
				driver.get(url);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

				// iframe 처리
				if (config.isRequiresFrame()) {
					wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(config.getFrameId()));
				}

				// ContentItems에서 첫 번째 이미지 찾기
				String imageUrl = extractor.extractContent(wait, config).stream()
					.filter(item -> item.getType() == ContentItemType.IMAGE)
					.findFirst()
					.map(ContentItem::getContent)
					.orElse("");

				// S3에 업로드
				if (!imageUrl.isEmpty()) {
					return imageStorageService.uploadImage(imageUrl);
				}

				return "";
			} catch (Exception e) {
				log.error("Failed to extract image from URL: {}", url, e);
				return "";
			} finally {
				if (config != null && config.isRequiresFrame()) {
					driver.switchTo().defaultContent();
				}
			}
		});
	}
}