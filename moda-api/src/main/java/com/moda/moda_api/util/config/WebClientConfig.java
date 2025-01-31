package com.moda.moda_api.util.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.baseUrl("https://tool.lilys.ai").build();
	}
}
