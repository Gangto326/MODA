package com.moda.moda_api.summary.infrastructure.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.moda.moda_api.summary.infrastructure.dto.YoutubeAPIResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class YoutubeApiClient {
	private final WebClient youtubeWebClient;

	@Value("${youtube.api.key}")
	private String youtubeApiKey;

	public CompletableFuture<YoutubeAPIResponseDTO> getVideoData(String videoId) {
		log.info("유튜브 분석하기직전");

		return getVideoInfo(videoId)
			.thenCompose(videoData ->
				getChannelData(extractChannelId(videoData))
					.thenApply(channelData ->
						YoutubeAPIResponseDTO.builder()
							.title(extractVideoTitle(videoData))
							.tags(extractTags(videoData))
							.description(extractDescription(videoData))
							.channelTitle(extractChannelTitle(channelData))
							.channelThumbnailUrl(extractChannelThumbnailUrl(channelData))
							.build()
					)
			);
	}

	private String extractVideoTitle(JsonNode videoData) {
		return videoData.path("items").get(0).path("snippet").path("title").asText();
	}

	private CompletableFuture<JsonNode> getVideoInfo(String videoId) {
		return youtubeWebClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.path("/videos")
				.queryParam("part", "snippet,statistics,contentDetails")
				.queryParam("id", videoId)
				.queryParam("key", youtubeApiKey)
				.build())
			.retrieve()
			.bodyToMono(JsonNode.class)
			.toFuture();
	}

	private CompletableFuture<JsonNode> getChannelData(String channelId) {
		return youtubeWebClient
			.get()
			.uri(uriBuilder -> uriBuilder
				.path("/channels")
				.queryParam("part", "snippet,statistics,brandingSettings")
				.queryParam("id", channelId)
				.queryParam("key", youtubeApiKey)
				.build())
			.retrieve()
			.bodyToMono(JsonNode.class)
			.toFuture();
	}

	private String[] extractTags(JsonNode videoData) {
		JsonNode tagsNode = videoData.path("items").get(0).path("snippet").path("tags");
		if (tagsNode.isMissingNode() || tagsNode.isNull()) {
			return new String[0];
		}

		return StreamSupport.stream(tagsNode.spliterator(), false)
			.map(JsonNode::asText)
			.toArray(String[]::new);
	}

	private String extractChannelId(JsonNode videoData) {
		return videoData.path("items").get(0).path("snippet").path("channelId").asText();
	}

	private String extractDescription(JsonNode videoData) {
		return videoData.path("items").get(0).path("snippet").path("description").asText();
	}

	private String extractChannelTitle(JsonNode channelData) {
		return channelData.path("items").get(0).path("snippet").path("title").asText();
	}

	private String extractChannelThumbnailUrl(JsonNode channelData) {
		return channelData.path("items").get(0).path("snippet")
			.path("thumbnails").path("high").path("url").asText();
	}
}