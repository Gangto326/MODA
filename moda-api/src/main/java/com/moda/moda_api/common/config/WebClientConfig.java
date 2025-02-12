package com.moda.moda_api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	@Value("${lilys.ai.api.key}")
	private String lilysApiKey;

	@Value("${PYTHON_AI_API_URL}")
	private String pythonURL;

	private String youtubeUrl = "https://www.googleapis.com/youtube/v3";

	@Bean("googleWebClient")
	public WebClient googleCustomSearchWebClient(WebClient.Builder builder) {
		return builder
			.baseUrl("https://www.googleapis.com/customsearch/v1")
			.defaultHeader("Content-Type", "application/json")
			.build();
	}

	@Bean("lilysWebClient")
	public WebClient lilysWebClient(WebClient.Builder builder) {
		return builder
			.defaultHeader("Content-Type", "application/json") // 타입 지정
			.defaultHeader("Authorization", "Bearer " + lilysApiKey) // apiKey 넣기
			.build();
	}

	@Bean("pythonWebClient")
	public WebClient pythonWebClient(WebClient.Builder builder) {
		return builder.baseUrl(pythonURL)
			.clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
			.build();
	}

	@Bean("youtubeWebClient")
	public WebClient youtubeWebClient(WebClient.Builder builder) {
		return builder
			.baseUrl(youtubeUrl) // 유튜브 API 기본 URL
			.defaultHeader("Content-Type", "application/json")
			.build();
	}
}
