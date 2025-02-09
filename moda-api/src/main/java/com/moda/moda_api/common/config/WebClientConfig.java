package com.moda.moda_api.common.config;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	@Value("${lilys.ai.api.key}")
	private String lilysApiKey;

	@Value("${PythonAI.api.url}")
	private String pythonURL;

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
}
