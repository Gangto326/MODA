package com.moda.moda_api.crawling.infrastructure.crawl;

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
import com.moda.moda_api.crawling.domain.model.Url;
import com.moda.moda_api.card.domain.Content;
import com.moda.moda_api.card.domain.ContentType;
import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.crawling.infrastructure.config.crawlerConfig.ExtractorConfig;
import com.moda.moda_api.crawling.infrastructure.config.crawlerConfig.PlatformExtractorFactory;
import com.moda.moda_api.util.exception.ExtractorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AbstractExtractor {
	private final WebDriver driver;
	private final PlatformExtractorFactory extractorFactory;
	private final ImageStorageService imageStorageService;

	public List<Url> extarctUrl(String url) {
		try{
			// 설정 방식 가져오기
			ExtractorConfig config = extractorFactory.getConfig(url);
			driver.get(url);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.cssSelector(config.getUrlSelector())
			));
			return elements.stream()
				.map(element -> {
					return new Url(element.getAttribute("href"));
				})
				.filter(extractedUrl -> extractedUrl != null && !extractedUrl.getValue().isEmpty())
				.collect(Collectors.toList());
		}
		catch (Exception e) {
			log.error("URL 추출 중 오류 발생: {}", url, e);
			throw new ExtractorException("URL 추출 실패", e);
		}
	}

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
				.urlDomainType(config.getUrlDomainType())
				.title(extractTitle())
				.contents(extractContent(wait, config))
				.build();

		} catch (Exception e) {
			throw new Exception("Failed to crawl content: " + e.getMessage());
		} finally {
			if (driver.switchTo() != null) {
				driver.switchTo().defaultContent();
			}
		}
	}

	public List<Content> extractContent(WebDriverWait wait, ExtractorConfig config) {
		List<CompletableFuture<Content>> futures = new ArrayList<>();

		try {
			WebDriverWait contentWait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement contentElement = contentWait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(config.getContentSelector())
			));

			// XPath를 수정하여 더 많은 텍스트 요소를 포함
			contentWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath(".//p[not(ancestor::p)]|.//img|.//li|.//ul")
			));

			// 텍스트를 포함할 수 있는 모든 요소 선택
			List<WebElement> elements = contentElement.findElements(
				By.xpath(".//p[not(ancestor::p)]|.//img|.//li[not(li)]")  // 중첩된 li는 제외
			);


			for (WebElement element : elements) {
				try {
					if (element.getTagName().equals("img")) {
						futures.add(CompletableFuture.supplyAsync(() -> {
							try {
								String src = element.getAttribute("src");
								if (src != null && !src.isEmpty() && isValidImageUrl(src)) {
									String savedImageUrl = imageStorageService.uploadImage(src);
									return new Content(savedImageUrl, ContentType.IMAGE);
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
								new Content(text, ContentType.TEXT)
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