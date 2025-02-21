package com.moda.moda_api.crawling.infrastructure.crawl;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TitleExtractor {
	private final WebDriver webDriver;

	public CompletableFuture<String> extractTitle(String url) {

		return CompletableFuture.supplyAsync(() -> {

			try {
				System.out.println("Extracting title for: " + url);

				// Open the URL
				webDriver.get(url);

				// Wait for the page to load completely
				WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(50));
				wait.until(webDriver -> ((JavascriptExecutor)webDriver)
					.executeScript("return document.readyState").equals("complete"));

				// Try getting the title
				String title = webDriver.getTitle();

				// If title is empty, check meta og:title
				if (title == null || title.isEmpty()) {
					List<WebElement> metaTags = webDriver.findElements(By.cssSelector("meta[property='og:title']"));
					if (!metaTags.isEmpty()) {
						title = metaTags.get(0).getAttribute("content");
					}
				}

				System.out.println("Extracted title: " + title);
				return (title != null && !title.isEmpty()) ? title : "";

			} catch (Exception e) {
				System.err.println("Failed to extract title from URL: " + url);
				e.printStackTrace();
				return "";
			} finally {
				if (webDriver != null) {
					webDriver.close(); // quit() 대신 close() 사용
				}
			}
		}, Executors.newSingleThreadExecutor()); // 단일 스레드 실행
	}
}
