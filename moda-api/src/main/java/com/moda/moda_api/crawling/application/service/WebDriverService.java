package com.moda.moda_api.crawling.application.service;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebDriverService {
	private final ChromeOptions chromeOptions;

	public WebDriver createDriver() {
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(15));
		log.info("Created new WebDriver session: {}", driver.getSessionId());
		return driver;
	}

	public void quitDriver(WebDriver driver) {
		if (driver == null) {
			return;
		}
		try {
			driver.quit();
		} catch (Exception e) {
			log.warn("WebDriver quit failed (session may already be closed): {}", e.getMessage());
		}
	}
}
