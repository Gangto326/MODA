package com.moda.moda_api.common.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
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
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
			.responseTimeout(Duration.ofSeconds(15));

		return builder
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.baseUrl("https://www.googleapis.com/customsearch/v1")
			.defaultHeader("Content-Type", "application/json")
			.build();
	}

	@Bean("lilysWebClient")
	public WebClient lilysWebClient(WebClient.Builder builder) {
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
			.responseTimeout(Duration.ofSeconds(30));

		return builder
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.defaultHeader("Content-Type", "application/json")
			.defaultHeader("Authorization", "Bearer " + lilysApiKey)
			.build();
	}

	@Bean("pythonWebClient")
	public WebClient pythonWebClient(WebClient.Builder builder) {
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
			.responseTimeout(Duration.ofSeconds(60))
			.followRedirect(true);

		return builder.baseUrl(pythonURL)
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.build();
	}

	@Bean("youtubeWebClient")
	public WebClient youtubeWebClient(WebClient.Builder builder) {
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
			.responseTimeout(Duration.ofSeconds(15));

		return builder
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.baseUrl(youtubeUrl)
			.defaultHeader("Content-Type", "application/json")
			.build();
	}
}
