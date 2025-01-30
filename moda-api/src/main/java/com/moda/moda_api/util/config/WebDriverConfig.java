package com.moda.moda_api.util.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebDriverConfig {
	@Bean
	public WebDriver chromeDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
		return new ChromeDriver(options);
	}
}