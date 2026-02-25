package com.moda.moda_api.common.config;

import java.time.Duration;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {
	private final ChromeOptions chromeOptions;

	@Override
	public WebDriver create() {
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(15));
		log.info("WebDriver pool: created new instance (session: {})", driver.getSessionId());
		return driver;
	}

	@Override
	public PooledObject<WebDriver> wrap(WebDriver driver) {
		return new DefaultPooledObject<>(driver);
	}

	@Override
	public void destroyObject(PooledObject<WebDriver> p, DestroyMode mode) {
		WebDriver driver = p.getObject();
		try {
			driver.quit();
			log.info("WebDriver pool: destroyed instance");
		} catch (Exception e) {
			log.warn("WebDriver pool: destroy failed (session may already be closed): {}", e.getMessage());
		}
	}

	@Override
	public boolean validateObject(PooledObject<WebDriver> p) {
		try {
			p.getObject().getTitle();
			return true;
		} catch (Exception e) {
			log.warn("WebDriver pool: validation failed, will be destroyed: {}", e.getMessage());
			return false;
		}
	}

	@Override
	public void passivateObject(PooledObject<WebDriver> p) {
		WebDriver driver = p.getObject();
		try {
			driver.manage().deleteAllCookies();
			driver.get("about:blank");
		} catch (Exception e) {
			log.warn("WebDriver pool: passivation failed: {}", e.getMessage());
		}
	}
}
