package com.moda.moda_api.common.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebDriverPoolConfig {
	private GenericObjectPool<WebDriver> pool;

	@Bean
	public GenericObjectPool<WebDriver> webDriverPool(ChromeOptions chromeOptions, MeterRegistry registry) {
		GenericObjectPoolConfig<WebDriver> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(3);
		config.setMaxIdle(2);
		config.setMinIdle(1);
		config.setTestOnBorrow(true);
		config.setTestWhileIdle(true);
		config.setMaxWait(Duration.ofSeconds(30));
		config.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));

		pool = new GenericObjectPool<>(new WebDriverFactory(chromeOptions), config);

		registry.gauge("moda.webdriver.pool.active", pool, GenericObjectPool::getNumActive);
		registry.gauge("moda.webdriver.pool.idle", pool, GenericObjectPool::getNumIdle);
		registry.gauge("moda.webdriver.pool.waiting", pool, GenericObjectPool::getNumWaiters);

		log.info("WebDriver pool initialized: maxTotal={}, maxIdle={}, minIdle={}",
			config.getMaxTotal(), config.getMaxIdle(), config.getMinIdle());

		return pool;
	}

	@PreDestroy
	public void destroy() {
		if (pool != null) {
			log.info("Shutting down WebDriver pool (active: {}, idle: {})",
				pool.getNumActive(), pool.getNumIdle());
			pool.close();
		}
	}
}
