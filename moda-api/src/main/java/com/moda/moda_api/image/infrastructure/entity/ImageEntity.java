package com.moda.moda_api.image.infrastructure.entity;

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
@Table(name = "images")
public class ImageEntity {
	@Id
	@Column(name = "images_id", length = 36, nullable = false)
	private String imageId;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "category_id", length = 36, nullable = false)
	private Long categoryId;

	@Column(name = "image_url" , length = 255, nullable = false)
	private String image_url;

	@Column(name = "embedding", columnDefinition = "VECTOR(768)")
	@Convert(converter = VectorConverter.class)
	private float[] embedding;

	@Column(name = "view_count", nullable = false)
	@Builder.Default
	private Integer viewCount = 0;

	@Type(StringArrayType.class)
	@Column(name = "keywords", columnDefinition = "text[]")
	private String[] keywords;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", insertable = false, updatable = false)
	private CategoryEntity category;

	@Column(name = "created_at", nullable = false, updatable = false)
	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();
}
