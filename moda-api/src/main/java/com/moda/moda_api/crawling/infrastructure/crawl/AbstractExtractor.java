package com.moda.moda_api.crawling.infrastructure.crawl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.moda.moda_api.common.infrastructure.ImageStorageService;
import com.moda.moda_api.crawling.domain.model.Url;
import com.moda.moda_api.summary.domain.ContentItem;
import com.moda.moda_api.summary.domain.ContentItemType;
import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.crawling.infrastructure.config.crawlerConfig.ExtractorConfig;
import com.moda.moda_api.crawling.infrastructure.config.crawlerConfig.PlatformExtractorFactory;
import com.moda.moda_api.summary.exception.ExtractorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AbstractExtractor {
	private final PlatformExtractorFactory extractorFactory;
	private final ImageStorageService imageStorageService;
	private final WebDriver driver;

	// URL을 추출하는 Method
	public List<Url> extractUrl(String url) {
		try {
			ExtractorConfig config = extractorFactory.getConfig(url);
			driver.get(url);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));

			// 페이지 로딩을 위한 초기 대기 추가
			Thread.sleep(1000);

			// 먼저 페이지가 완전히 로드될 때까지 대기
			wait.until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));

			// 그 다음 요소들을 찾음
			List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.cssSelector(config.getUrlSelector())
			));

			// 디버깅을 위한 로깅 추가
			log.debug("셀렉터와 일치하는 요소 {}개 발견", elements.size());

			return elements.stream()
				.map(element -> {
					String href = element.getAttribute("href");
					if (href != null && !href.isEmpty()) {
						return new Url(href);
					}
					return null;
				})
				.filter(Objects::nonNull)
				.filter(extractedUrl -> !extractedUrl.getValue().isEmpty())
				.distinct()  // 중복 제거
				.collect(Collectors.toList());

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ExtractorException("URL 추출 중 쓰레드 중단됨", e);
		} catch (Exception e) {
			log.error("URL 추출 중 오류 발생: {}", url, e);
			throw new ExtractorException("URL 추출 실패", e);
		}
	}

	// 추출하는 매서드
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
				.url(new Url(url))
				.urlDomainType(config.getUrlDomainType())
				.title(extractTitle())
				.contentItems(extractContent(config))
				.build();

		} catch (Exception e) {
			throw new Exception("Failed to crawl content: " + e.getMessage());
		} finally {
			if (driver.switchTo() != null) {
				driver.switchTo().defaultContent();
			}
		}
	}

	//Body본문과 이미지 추출하기
	public List<ContentItem> extractContent(ExtractorConfig config) {
		List<ContentItem> result = new ArrayList<>();
		StringBuilder textBuilder = new StringBuilder();

		try {
			WebDriverWait contentWait = new WebDriverWait(driver, Duration.ofSeconds(20));
			WebElement contentElement = contentWait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(config.getContentSelector())
			));

			// 모든 콘텐츠 요소들이 로드될 때까지 대기
			contentWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
				By.xpath(".//h1|.//h2|.//h3|.//h4|.//h5|.//h6|.//p[not(ancestor::p)]|.//img|.//li|.//ul")
			));

			// 텍스트와 이미지를 포함하는 모든 요소 선택
			List<WebElement> elements = contentElement.findElements(
				By.xpath(".//h1|.//h2|.//h3|.//h4|.//h5|.//h6|.//p[not(ancestor::p)]|.//img|.//li[not(li)]")
			);

			for (WebElement element : elements) {
				try {
					String tagName = element.getTagName().toLowerCase();

					if (tagName.equals("img")) {
						// 이미지를 만나면 지금까지 모은 텍스트를 저장
						if (textBuilder.length() > 0) {
							result.add(new ContentItem(textBuilder.toString().trim(), ContentItemType.TEXT));
							textBuilder.setLength(0); // StringBuilder 초기화
						}

						// 이미지 처리
						String src = element.getAttribute("src");
						if (src != null && !src.isEmpty() && isValidImageUrl(src)) {
							try {
								String savedImageUrl = imageStorageService.uploadImage(src);
								result.add(new ContentItem(savedImageUrl, ContentItemType.IMAGE));
							} catch (Exception e) {
								log.error("Failed to process image: {}", src, e);
							}
						}
					} else {
						// 텍스트 요소 처리
						String text = element.getText().trim();
						if (!text.isEmpty()) {
							// 헤딩 태그인 경우 태그 정보 포함
							if (tagName.matches("h[1-6]")) {
								if (textBuilder.length() > 0) {
									textBuilder.append("\n");
								}
								textBuilder.append(String.format("<%s>%s</%s>", tagName, text, tagName));
							} else {
								// 일반 텍스트의 경우
								if (textBuilder.length() > 0) {
									textBuilder.append("\n");
								}
								textBuilder.append(text);
							}
						}
					}
				} catch (StaleElementReferenceException e) {
					log.warn("Encountered stale element", e);
					continue;
				}
			}

			// 마지막 텍스트 블록 처리
			if (textBuilder.length() > 0) {
				result.add(new ContentItem(textBuilder.toString().trim(), ContentItemType.TEXT));
			}

			return result;

		} catch (Exception e) {
			log.error("Failed to extract content", e);
			return result;
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