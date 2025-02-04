package com.moda.moda_api.card.infrastructure.entity;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

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

    @Column(name = "cached_title", length = 100, nullable = false)
    private String cachedTitle;

    @Column(name = "cached_content", columnDefinition = "TEXT", nullable = false)
    private String cachedContent;

    @Type(StringArrayType.class)
    @Column(columnDefinition = "text[]")
    private String[] keywords;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
