package com.moda.moda_api.crawling.application.service;

import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebDriverService {
	private final WebDriver webDriver;
	private final ChromeOptions chromeOptions;

	public WebDriver getDriver() {
		try {
			webDriver.getCurrentUrl();
			return webDriver;
		} catch (NoSuchSessionException e) {
			log.warn("Session expired. Recreating new WebDriver session...");
			try {
				webDriver.quit(); // 기존 세션 정리
			} catch (Exception ex) {
				log.error("Error while closing expired session", ex);
			}

			// 새로운 ChromeDriver 세션 생성
			WebDriver newDriver = new ChromeDriver(chromeOptions);

			// 새로운 세션 정보 로깅
			String sessionId = ((ChromeDriver) newDriver).getSessionId().toString();
			log.info("Created new WebDriver session: {}", sessionId);

			return newDriver;
		}
	}

	@PreDestroy
	public void cleanUp() {
		if (webDriver != null) {
			try {
				webDriver.quit();
			} catch (Exception e) {
				log.error("Error during WebDriver cleanup", e);
			}
		}
	}
}