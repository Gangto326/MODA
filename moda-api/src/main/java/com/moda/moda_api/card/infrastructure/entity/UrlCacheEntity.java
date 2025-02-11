package com.moda.moda_api.card.infrastructure.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Type;

import com.moda.moda_api.card.infrastructure.converter.VectorConverter;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "url_caches")
public class UrlCacheEntity {
	@Id
	@Column(name = "url_hash", length = 64, nullable = false)
	private String urlHash;

	@Column(name = "original_url", columnDefinition = "TEXT", nullable = false)
	private String originalUrl;

	@Column(name = "type_id")
	private Integer typeId;

	@Column(name = "category_id", length = 36, nullable = false)
	private Long categoryId;

	@Column(name = "cached_title", length = 100, nullable = false)
	private String cachedTitle;

	@Column(name = "cached_content", columnDefinition = "TEXT", nullable = false)
	private String cachedContent;

	@Column(name = "cached_thumbnail_content", columnDefinition = "TEXT")
	private String cachedThumbnailContent;

	@Column(name = "cached_thumbnail_url")
	private String cachedThumbnailUrl;

	@Column(name = "cached_embedding", columnDefinition = "VECTOR(768)")
	@Convert(converter = VectorConverter.class)
	private float[] cachedEmbedding;

	@Type(StringArrayType.class)
	@Column(name = "cached_keywords", columnDefinition = "text[]")
	private String[] cachedKeywords;

	@Type(StringArrayType.class)
	@Column(name = "cached_sub_contents", columnDefinition = "text[]")
	private String[] cachedSubContents;

	@Column(name = "created_at", nullable = false, updatable = false)
	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();
}
