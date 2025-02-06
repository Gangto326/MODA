package com.moda.moda_api.crawling.infrastructure.config.searchConfig;

public interface SearchStrategy {
	String buildSearchQuery(String keyword);
}