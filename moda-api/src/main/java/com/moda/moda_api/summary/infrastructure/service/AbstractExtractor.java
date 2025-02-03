package com.moda.moda_api.summary.infrastructure.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.domain.crawler.CrawledContent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AbstractExtractor {
	private final WebDriver driver;
	private final PlatformExtractorFactory extractorFactory;

	public CrawledContent extract(String url) throws Exception {
		try {
			ExtractorConfig config = extractorFactory.getConfig(url);
			driver.get(url);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// iframe 처리
			if (config.isRequiresFrame()) {
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(config.getFrameId()));
			}

			return CrawledContent.builder()
				.url(url)
				.crawledContentType(config.getCrawledContentType())
				.title(extractTitle())
				.content(extractContent(wait, config))
				.imageUrls(extractImages(wait, config))
				.build();
		} catch (Exception e) {
			throw new Exception("Failed to crawl content: " + e.getMessage());
		} finally {
			if (driver.switchTo() != null) {
				driver.switchTo().defaultContent();
			}
		}
	}

	private String extractTitle() {
		return driver.getTitle();
	}

	private String extractContent(WebDriverWait wait, ExtractorConfig config) {
		try {
			WebElement contentElement = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(config.getContentSelector())
			));

			try {
				List<WebElement> imageDescs = contentElement.findElements(By.className("img_desc"));
				for (WebElement desc : imageDescs) {
					((JavascriptExecutor)driver).executeScript("arguments[0].remove();", desc);
				}
			} catch (Exception e) {
				// 이미지 설명이 없는 경우 무시
			}

			return contentElement.getText();
		} catch (Exception e) {
			log.error("Failed to extract content", e);
			return "";
		}
	}

	//이거 외부에서 쓰기위해서 잠시 public으로 돌리는데 어떤 문제점이 생길지는 모르겠어.
	public List<String> extractImages(WebDriverWait wait, ExtractorConfig config) {
		List<String> images = new ArrayList<>();
		try {
			List<WebElement> imageElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.cssSelector(config.getImageSelector())
			));

			for (WebElement img : imageElements) {
				try {
					String src = img.getAttribute("src");
					if (src == null || src.isEmpty()) {
						src = img.getAttribute("data-src");
					}
					if (src != null && !src.isEmpty() && isValidImageUrl(src)) {
						images.add(src);
					}
				} catch (StaleElementReferenceException e) {
				}
			}
		} catch (TimeoutException e) {
			log.warn("Timeout waiting for images: {}", e.getMessage());
		}
		return images;
	}

	private boolean isValidImageUrl(String url) {
		return url.matches(".*\\.(jpg|jpeg|png|gif|webp)($|\\?.*)") &&
			!url.contains("favicon") &&
			!url.contains("logo") &&
			url.length() > 10;
	}
}