package com.moda.moda_api.common.config;

import java.util.Arrays;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {

	@Bean
	public ChromeOptions chromeOptions() {
		ChromeOptions options = new ChromeOptions();
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
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);



		options.addArguments("--disk-cache-size=100000000");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--disable-web-security");
		options.addArguments("--disable-popup-blocking");
		return options;
	}

}