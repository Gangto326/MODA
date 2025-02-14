package com.moda.moda_api.card.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.card.domain.UrlCache;
import com.moda.moda_api.card.infrastructure.entity.UrlCacheEntity;
import com.moda.moda_api.category.domain.CategoryId;

@Component
public class UrlCacheEntityMapper {

	public UrlCache toDomain(UrlCacheEntity entity) {
		return UrlCache.builder()
			.urlHash(entity.getUrlHash())
			.originalUrl(entity.getOriginalUrl())
			.typeId(entity.getTypeId())
			.categoryId(new CategoryId(entity.getCategoryId()))
			.cachedTitle(entity.getCachedTitle())
			.cachedContent(entity.getCachedContent())
			.cachedThumbnailContent(entity.getCachedThumbnailContent())
			.cachedThumbnailUrl(entity.getCachedThumbnailUrl())
			.cachedEmbedding(new EmbeddingVector(entity.getCachedEmbedding()))
			.cachedKeywords(entity.getCachedKeywords())
			.cachedSubContents(entity.getCachedKeywords())
			.createdAt(entity.getCreatedAt())
			.build();
	}

	public UrlCacheEntity toEntity(UrlCache domain) {
		return UrlCacheEntity.builder()
			.urlHash(domain.getUrlHash())
			.originalUrl(domain.getOriginalUrl())
			.typeId(domain.getTypeId())
			.categoryId(domain.getCategoryId().getValue())
			.cachedTitle(domain.getCachedTitle())
			.cachedContent(domain.getCachedContent())
			.cachedThumbnailUrl(domain.getCachedThumbnailUrl())
			.cachedThumbnailContent(domain.getCachedThumbnailContent())

			.cachedEmbedding(domain.getCachedEmbedding().getValues())

			.cachedKeywords(domain.getCachedKeywords())
			.cachedSubContents(domain.getCachedSubContents())
			.createdAt(domain.getCreatedAt())
			.build();
	}
}