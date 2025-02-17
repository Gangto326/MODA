package com.moda.moda_api.crawling.infrastructure.crawl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import com.moda.moda_api.crawling.application.service.WebDriverService;
import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.crawling.domain.model.ExtractedContent;
import com.moda.moda_api.crawling.domain.model.Url;
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
	private final WebDriverService webDriverService;

	// URL을 추출하는 Method
	public List<Url> extractUrl(String url) {
		WebDriver driver = null;

		try {
			driver = webDriverService.getDriver();

			ExtractorConfig config = extractorFactory.getConfig(url);
			driver.get(url);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(webDriver -> ((JavascriptExecutor)webDriver)
				.executeScript("return document.readyState").equals("complete"));

			// 페이지 로딩을 위한 초기 대기 추가
			Thread.sleep(1000);
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
		WebDriver driver = null;

		try {
			driver = webDriverService.getDriver();

			ExtractorConfig config = extractorFactory.getConfig(url);
			System.out.println(config.getPattern());

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
				.title(extractTitle(driver))  // driver 전달
				.extractedContent(extractContent(config, driver))  // driver 전달
				.build();

		} catch (Exception e) {
			throw new Exception("Failed to crawl content: " + e.getMessage());
		} finally {
			if (driver != null && driver.switchTo() != null) {  // driver null 체크 추가
				driver.switchTo().defaultContent();
			}
		}
	}

	//Body본문과 이미지 추출하기
	public ExtractedContent extractContent(ExtractorConfig config,WebDriver driver) {
		StringBuilder textBuilder = new StringBuilder();
		List<String> imagesList = new ArrayList<>();  // 임시로 List 사용

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
						// 이미지 처리
						String src = element.getAttribute("src");
						if (src != null && !src.isEmpty() && isValidImageUrl(src)) {
							try {
								String savedImageUrl = imageStorageService.uploadImageFromurl(src);
								imagesList.add(savedImageUrl);
							} catch (Exception e) {
								log.error("Failed to process image: {}", src, e);
							}
						}
					} else {
						// 텍스트 요소 처리
						String text = element.getText().trim();
						if (!text.isEmpty()) {
							// 이전 텍스트가 있으면 줄바꿈 추가
							if (textBuilder.length() > 0) {
								textBuilder.append("\n");
							}

							// 헤딩 태그인 경우 태그 정보 포함
							if (tagName.matches("h[1-6]")) {
								textBuilder.append(String.format("<%s>%s</%s>", tagName, text, tagName));
							} else {
								textBuilder.append(text);
							}
						}
					}
				} catch (StaleElementReferenceException e) {
					log.warn("Encountered stale element", e);
					continue;
				}
			}

			return ExtractedContent.builder()
				.text(textBuilder.toString().trim())
				.images(imagesList.toArray(new String[0]))  // List를 배열로 변환
				.build();

		} catch (Exception e) {
			log.error("Failed to extract content", e);
			return ExtractedContent.empty();
		}
	}

	private String extractTitle(WebDriver driver) {
		return driver.getTitle();
	}
	private boolean isValidImageUrl(String url) {
		return url.matches(".*\\.(jpg|jpeg|png|gif|webp)($|\\?.*)") &&
			!url.contains("favicon") &&
			!url.contains("logo") &&
			url.length() > 10;
	}

}