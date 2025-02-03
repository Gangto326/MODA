package com.moda.moda_api.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${lilys.ai.api.key}")
	private String lilysApiKey;

	@Value("${embedding.api.url}")
	private String embeddingUrl;

	@Bean
	public WebClient lilysWebClient(WebClient.Builder builder) {
		return builder
			.defaultHeader("Content-Type", "application/json") // 타입 지정
			.defaultHeader("Authorization", "Bearer " + lilysApiKey) // apiKey 넣기
			.build();
	}

	@Bean
	public WebClient pythonWebClient(WebClient.Builder builder) {
		return builder.baseUrl(embeddingUrl).build();
	}
}
