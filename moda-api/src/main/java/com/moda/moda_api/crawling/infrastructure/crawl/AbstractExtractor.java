package com.moda.moda_api.crawling.infrastructure.crawl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.moda.moda_api.common.exception.ContentExtractionException;
import com.moda.moda_api.common.infrastructure.ImageStorageService;
import com.moda.moda_api.crawling.application.service.WebDriverService;
import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.crawling.domain.model.ExtractedContent;
import com.moda.moda_api.crawling.domain.model.Url;
import com.moda.moda_api.crawling.domain.model.UrlDomainType;
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
			log.info("URL: {}", url);  // URL 로깅
			log.info("Selected Pattern: {}", config.getPattern());  // 매칭된 패턴 로깅
			log.info("Content Selector: {}", config.getContentSelector());  // 선택자 로깅

			driver.get(url);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
	public CrawledContent extract(String url) throws TimeoutException , NoSuchElementException  {
		WebDriver driver = null;

		try {
			driver = webDriverService.getDriver();

			ExtractorConfig config = extractorFactory.getConfig(url);
			System.out.println(config.getPattern());

			driver.get(url);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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

		}catch (TimeoutException | NoSuchElementException e) {
			log.error("Failed to find element while crawling: {}", e.getMessage());
			throw e;  // 이 예외들도 상위로 전파
		} catch (Exception e) {
			log.error("Failed to extract content", e);
			throw e;  // 상위 메서드로 예외를 그대로 전달
		}
	}

	//Body본문과 이미지 추출하기
	public ExtractedContent extractContent(ExtractorConfig config, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			// 컨텐츠 요소 찾기
			WebElement contentElement = wait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector(config.getContentSelector())
			));

			log.info("Trying to find content with selector: {}", config.getContentSelector());

			log.info("Found content element: {}", contentElement.getAttribute("class"));

			// 텍스트 추출 - 전체 텍스트를 한 번에 가져옴
			String text = contentElement.getText().trim();

			// 이미지 URL 추출 및 저장
			String[] images = contentElement.findElements(By.tagName("img"))
				.stream()
				.map(img -> img.getAttribute("src"))
				.filter(src -> src != null && !src.isEmpty() && isValidImageUrl(src))
				.map(src -> {
					try {
						return imageStorageService.uploadImageFromurl(src);
					} catch (Exception e) {
						log.error("Failed to upload image: {}", src, e);
						return null;
					}
				})
				.filter(Objects::nonNull)
				.toArray(String[]::new);

			return ExtractedContent.builder()
				.text(text)
				.images(images)
				.build();

		} catch (TimeoutException | NoSuchElementException | CompletionException e) {
			log.error("Failed to find element while crawling: {}", e.getMessage());
			throw e;  // 이 예외들도 상위로 전파
		} catch (Exception e) {
			log.error("Failed to extract content", e);
			throw e;  // 상위 메서드로 예외를 그대로 전달
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