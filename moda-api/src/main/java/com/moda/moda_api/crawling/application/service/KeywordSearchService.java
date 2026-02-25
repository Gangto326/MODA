package com.moda.moda_api.crawling.application.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.moda.moda_api.crawling.domain.model.Url;
import com.moda.moda_api.crawling.infrastructure.repository.UrlRedisRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KeywordSearchService {
	private final WebClient googleWebClient;
	private final UrlRedisRepository urlRedisRepository;

	@Value("${google.custom.search.api.key:}")
	private String apiKey;

	@Value("${google.custom.search.engine.id:}")
	private String engineId;

	public KeywordSearchService(
		@Qualifier("googleWebClient") WebClient googleWebClient,
		UrlRedisRepository urlRedisRepository) {
		this.googleWebClient = googleWebClient;
		this.urlRedisRepository = urlRedisRepository;
	}

	public List<Url> crawlByKeyWord(String keyword) {
		log.info("Google Custom Search API 검색: {}", keyword);

		List<Url> urls = searchViaApi(keyword);

		// Redis에 저장
		urlRedisRepository.saveUrls(keyword, urls);
		return urlRedisRepository.getUrls(keyword);
	}

	private List<Url> searchViaApi(String keyword) {
		if (apiKey == null || apiKey.isBlank() || engineId == null || engineId.isBlank()) {
			log.warn("Google Custom Search API 키 또는 엔진 ID 미설정 — 빈 결과 반환");
			return Collections.emptyList();
		}

		try {
			JsonNode response = googleWebClient.get()
				.uri(uriBuilder -> uriBuilder
					.queryParam("key", apiKey)
					.queryParam("cx", engineId)
					.queryParam("q", keyword)
					.queryParam("num", 10)
					.queryParam("lr", "lang_ko")
					.build())
				.retrieve()
				.bodyToMono(JsonNode.class)
				.block();

			if (response == null || !response.has("items")) {
				log.info("검색 결과 없음: {}", keyword);
				return Collections.emptyList();
			}

			List<Url> urls = StreamSupport.stream(response.get("items").spliterator(), false)
				.map(item -> item.get("link").asText())
				.filter(link -> link != null && !link.isEmpty())
				.distinct()
				.map(Url::new)
				.collect(Collectors.toList());

			log.info("Google Custom Search 결과: {}건", urls.size());
			return urls;

		} catch (Exception e) {
			log.error("Google Custom Search API 호출 실패: {}", e.getMessage());
			return Collections.emptyList();
		}
	}
}
