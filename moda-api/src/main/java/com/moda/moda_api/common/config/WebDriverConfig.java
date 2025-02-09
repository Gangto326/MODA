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

		// 봇 감지 우회를 위한 설정
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);

		// 일반적인 브라우저처럼 보이게 하는 설정
		options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--disable-gpu");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");

		// 새로 추가할 부분
		options.addArguments("--remote-allow-origins=*");  // WebDriver 최신 버전과 호환성을 위한 옵션
		options.addArguments("--start-maximized");  // 브라우저 최대화
		options.addArguments("--disable-extensions");  // 확장 프로그램 비활성화

		// 헤드리스 모드 설정 (선택사항)
		options.addArguments("--headless");  // 브라우저 창을 보이지 않게 하려면 이 줄의 주석을 해제하세요

		return new ChromeDriver(options);
	}
}