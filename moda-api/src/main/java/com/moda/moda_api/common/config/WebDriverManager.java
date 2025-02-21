package com.moda.moda_api.common.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverManager {
	private static WebDriver driver;

	// WebDriver 인스턴스를 가져오는 메서드
	public static WebDriver getDriver() {
		if (driver == null) {
			startWebDriver();
		} else {
			try {
				if (driver.getWindowHandles().isEmpty()) {
					restartWebDriver();
				}
			} catch (Exception e) {
				restartWebDriver();
			}
		}
		return driver;
	}

	// WebDriver 시작 메서드
	private static void startWebDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless"); // UI 없이 실행 (필요에 따라 변경)
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		driver = new ChromeDriver(options);
	}

	// WebDriver를 재시작하는 메서드
	public static void restartWebDriver() {
		stopWebDriver();
		startWebDriver();
	}

	// WebDriver를 종료하는 메서드
	public static void stopWebDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}
}