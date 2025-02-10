package com.moda.moda_api.summary.infrastructure.service;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TitleExtractor {
	private final WebDriver driver;

	public CompletableFuture<String> extractTitle(String url) {
		return CompletableFuture.supplyAsync(() -> {
			try {

				System.out.println("들어왔어 ");


				driver.get(url);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

				// 페이지 로드 완료 대기
				// wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

				// 제목 가져오기
				String title = driver.getTitle();
				if (title == null || title.isEmpty()) {
					// og:title 가져오기 (존재할 경우)
					var metaTags = driver.findElements(By.cssSelector("meta[property='og:title']"));
					if (!metaTags.isEmpty()) {
						title = metaTags.get(0).getAttribute("content");
					}
				}

				System.out.println("title : " + title);
;
				return title != null ? title : "";
			} catch (Exception e) {
				System.err.println("Failed to extract title from URL: " + url);
				e.printStackTrace();
				return "";
			}
		});
	}
}
