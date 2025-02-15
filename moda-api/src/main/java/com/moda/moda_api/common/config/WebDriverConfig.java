package com.moda.moda_api.common.config;

import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class WebDriverConfig {

	@Bean
	@Scope("prototype")
	public WebDriver webDriver() {
		ChromeOptions options = new ChromeOptions();

		// 기존 옵션 유지
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		options.addArguments("--disable-gpu");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--disable-extensions");
		options.addArguments("--headless");

		return new ChromeDriver(options);
	}
}