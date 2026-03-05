package com.moda.moda_api.common.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MetricsConfig {

	@Bean
	public Timer cardCreationTimer(MeterRegistry registry) {
		return Timer.builder("moda.card.creation.duration")
			.description("Card creation total duration")
			.register(registry);
	}

	@Bean
	public Counter cardCreationCounter(MeterRegistry registry) {
		return Counter.builder("moda.card.creation.total")
			.description("Card creation attempts")
			.register(registry);
	}

	@Bean
	public Counter cardCreationErrorCounter(MeterRegistry registry) {
		return Counter.builder("moda.card.creation.errors")
			.description("Card creation failures")
			.register(registry);
	}

	@Bean
	public Timer mainPageTimer(MeterRegistry registry) {
		return Timer.builder("moda.mainpage.duration")
			.description("getMainPage() duration")
			.register(registry);
	}

	@Bean
	public String monitorExecutors(
		MeterRegistry registry,
		@Qualifier("imageExecutor") Executor imageExecutor,
		@Qualifier("cardSaveExecutor") Executor cardSaveExecutor,
		@Qualifier("youtubeExecutor") Executor youtubeExecutor,
		ExecutorService executorService
	) {
		if (imageExecutor instanceof ThreadPoolTaskExecutor tpe) {
			ExecutorServiceMetrics.monitor(registry, tpe.getThreadPoolExecutor(), "imageExecutor");
		}
		if (cardSaveExecutor instanceof ThreadPoolTaskExecutor tpe) {
			ExecutorServiceMetrics.monitor(registry, tpe.getThreadPoolExecutor(), "cardSaveExecutor");
		}
		if (youtubeExecutor instanceof ThreadPoolTaskExecutor tpe) {
			ExecutorServiceMetrics.monitor(registry, tpe.getThreadPoolExecutor(), "youtubeExecutor");
		}
		ExecutorServiceMetrics.monitor(registry, executorService, "searchExecutorService");
		return "executors-monitored";
	}
}
