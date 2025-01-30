package com.moda.moda_api.summary.infrastructure;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.moda.moda_api.summary.domain.model.CrawledContent;
import com.moda.moda_api.summary.domain.model.Url;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DynamicWebCrawler implements WebCrawler {
	private final WebDriver driver;  // Selenium WebDriver

	@Override
	public CrawledContent crawl(Url url) throws Exception {
		try {
			driver.get(url.getValue());
			// JavaScript 실행 완료 대기
			Thread.sleep(2000);  // 또는 WebDriverWait 사용

			String title = driver.getTitle();
			String body = driver.findElement(By.tagName("body")).getText();

			return new CrawledContent(url.getValue(), title, body);
		} catch (Exception e) {
			throw new Exception("Failed to crawl dynamic URL: " + url.getValue(), e);
		}
	}
}