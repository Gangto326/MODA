package com.moda.moda_api.crawling.infrastructure.config.searchConfig;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.moda.moda_api.crawling.domain.model.SearchType;

// @Configuration
public class SearchConfig {
	// @Bean
	// public Map<SearchType, SearchStrategy> searchStrategies(
	// 	TistorySearchStrategy tistoryStrategy,
	// 	YoutubeSearchStrategy youtubeStrategy,
	// 	PdfSearchStrategy pdfStrategy) {
	//
	// 	Map<SearchType, SearchStrategy> strategies = new EnumMap<>(SearchType.class);
	// 	strategies.put(SearchType.TISTORY, tistoryStrategy);
	// 	strategies.put(SearchType.YOUTUBE, youtubeStrategy);
	// 	strategies.put(SearchType.PDF, pdfStrategy);
	// 	strategies.put(SearchType.GENERAL, keyword -> keyword); // 기본 검색
	//
	// 	return strategies;
	// }
}