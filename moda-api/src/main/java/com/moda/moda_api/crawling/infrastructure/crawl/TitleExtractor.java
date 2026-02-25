package com.moda.moda_api.crawling.infrastructure.crawl;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.moda.moda_api.crawling.application.service.WebDriverService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TitleExtractor {
	private final WebDriverService webDriverService;
	private final Executor crawlingExecutor;

	public TitleExtractor(
			WebDriverService webDriverService,
			@Qualifier("crawlingExecutor") Executor crawlingExecutor) {
		this.webDriverService = webDriverService;
		this.crawlingExecutor = crawlingExecutor;
	}

	public CompletableFuture<String> extractTitle(String url) {
		return CompletableFuture.supplyAsync(() -> {
			WebDriver driver = null;
			try {
				log.info("Extracting title for: {}", url);

				driver = webDriverService.borrowDriver();
				driver.get(url);

				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
				wait.until(d -> ((JavascriptExecutor)d)
					.executeScript("return document.readyState").equals("complete"));

				String title = driver.getTitle();

				if (title == null || title.isEmpty()) {
					List<WebElement> metaTags = driver.findElements(
						By.cssSelector("meta[property='og:title']"));
					if (!metaTags.isEmpty()) {
						title = metaTags.get(0).getAttribute("content");
					}
				}

				log.info("Extracted title: {}", title);
				return (title != null && !title.isEmpty()) ? title : "";

			} catch (Exception e) {
				log.error("Failed to extract title from URL: {}", url, e);
				return "";
			} finally {
				webDriverService.returnDriver(driver);
			}
		}, crawlingExecutor);
	}
}
