package com.moda.moda_api.card.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.moda.moda_api.card.domain.UrlCache;
import com.moda.moda_api.card.infrastructure.entity.UrlCacheEntity;

@Component
public class UrlCacheEntityMapper {
	public UrlCache toDomain(UrlCacheEntity entity) {
		return UrlCache.builder()
			.urlHash(entity.getUrlHash())
			.originalUrl(entity.getOriginalUrl())
			.cachedTitle(entity.getCachedTitle())
			.cachedContext(entity.getCachedContent())
			.createdAt(entity.getCreatedAt())
			.build();
	}

	public UrlCacheEntity toEntity(UrlCache domain) {
		return UrlCacheEntity.builder()
			.urlHash(domain.getUrlHash())
			.originalUrl(domain.getOriginalUrl())
			.cachedTitle(domain.getCachedTitle())
			.cachedContent(domain.getCachedContext())
			.createdAt(domain.getCreatedAt())
			.build();
	}
}