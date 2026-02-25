package com.moda.moda_api.crawling.application.service;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebDriverService {
	private final GenericObjectPool<WebDriver> webDriverPool;

	public WebDriver borrowDriver() {
		try {
			WebDriver driver = webDriverPool.borrowObject();
			log.info("WebDriver borrowed (active: {}, idle: {})",
				webDriverPool.getNumActive(), webDriverPool.getNumIdle());
			return driver;
		} catch (Exception e) {
			log.error("Failed to borrow WebDriver from pool: {}", e.getMessage());
			throw new RuntimeException("WebDriver pool exhausted or creation failed", e);
		}
	}

	public void returnDriver(WebDriver driver) {
		if (driver == null) {
			return;
		}
		try {
			webDriverPool.returnObject(driver);
			log.info("WebDriver returned (active: {}, idle: {})",
				webDriverPool.getNumActive(), webDriverPool.getNumIdle());
		} catch (Exception e) {
			log.warn("Failed to return WebDriver to pool, invalidating: {}", e.getMessage());
			invalidateDriver(driver);
		}
	}

	public void invalidateDriver(WebDriver driver) {
		if (driver == null) {
			return;
		}
		try {
			webDriverPool.invalidateObject(driver);
			log.info("WebDriver invalidated (active: {}, idle: {})",
				webDriverPool.getNumActive(), webDriverPool.getNumIdle());
		} catch (Exception e) {
			log.warn("Failed to invalidate WebDriver: {}", e.getMessage());
		}
	}
}
