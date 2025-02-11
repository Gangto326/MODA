package com.moda.moda_api.card.infrastructure.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Type;

import com.moda.moda_api.card.infrastructure.converter.VectorConverter;
import com.moda.moda_api.category.infrastructure.entity.CategoryEntity;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "cards")
public class CardEntity {
    @Id
    @Column(name = "card_id", length = 36, nullable = false)
    private String cardId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "category_id", length = 36, nullable = false)
    private Long categoryId;

    @Column(name = "type_id", nullable = false)
    private Integer typeId;

    @Column(name = "url_hash", length = 64, nullable = false)
    private String urlHash;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "thumbnail_content", columnDefinition = "TEXT", nullable = false)
    private String thumbnailContent;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT", nullable = false)
    private String thumbnailUrl;

    @Column(name = "embedding", columnDefinition = "VECTOR(768)")
    @Convert(converter = VectorConverter.class)
    private float[] embedding;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(name = "bookmark", nullable = false)
    @Builder.Default
    private Boolean bookmark = false;

    @Type(StringArrayType.class)
    @Column(name = "keywords", columnDefinition = "text[]")
    private String[] keywords;

    @Type(StringArrayType.class)
    @Column(name = "subContents", columnDefinition = "text[]")
    private String[] subContents;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    private ContentTypeEntity contentType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "url_hash", insertable = false, updatable = false)
    private UrlCacheEntity urlCache;
}
