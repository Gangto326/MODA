package com.moda.moda_api.summary.infrastructure.service;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.moda.moda_api.summary.domain.crawler.CrawledContent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
//이 클래스는 LilysAI에서 title과 image를 제공하지 않았기에 사용합니다.
// url을 크롤링을 통해 접근하고 title과 이미지를 가져옵니다.
public class TitleAndImageExtractor {
	private final WebDriver driver;
	private final AbstractExtractor extractor;
	private final PlatformExtractorFactory extractorFactory;

	public CompletableFuture<String> extractTitleAsync(String url) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				driver.get(url);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

				// title tag가 나올때까지 기다려주쇼
				wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("title")));
				wait.until(driver -> !driver.getTitle().isEmpty());

				String title = driver.getTitle();
				//보통은 title태그 또는 og:title tag에 둘다 있다.
				if (title.isEmpty()) {
					// title이 없는 경우 og:title 메타 태그 확인
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
			try {
				ExtractorConfig config = extractorFactory.getConfig(url);
				driver.get(url);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

				// iframe 처리
				if (config.isRequiresFrame()) {
					wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(config.getFrameId()));
				}

				List<String> imageUrls = extractor.extractImages(wait, config);

				// iframe에서 빠져나오기
				if (config.isRequiresFrame()) {
					driver.switchTo().defaultContent();
				}

				return imageUrls.isEmpty() ? "" : imageUrls.get(0);
			} catch (Exception e) {
				log.error("Failed to extract image from URL: {}", url, e);
				return "";
			}
		});
	}
}