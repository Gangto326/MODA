package com.moda.moda_api.image.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.image.domain.Image;
import com.moda.moda_api.image.domain.ImageId;
import com.moda.moda_api.image.infrastructure.entity.ImageEntity;
import com.moda.moda_api.user.domain.UserId;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImageEntityMapper {
	public ImageEntity toEntity(Image image) {
		return ImageEntity.builder()
			.imageId(image.getImageId().getValue())
			.userId(image.getUserId().getValue())
			.categoryId(image.getCategoryId().getValue())
			.imageUrl(image.getImageUrl())
			.embedding(image.getEmbedding().getValues())
			.keywords(image.getKeywords())
			.viewCount(image.getViewCount())
			.createdAt(image.getCreatedAt())
			.build();
	}

	public Image toDomain(ImageEntity entity) {
		log.debug("Entity keywords: {}", entity.getKeywords());

		Image image = Image.builder()
			.imageId(new ImageId(entity.getImageId()))
			.userId(new UserId(entity.getUserId()))
			.categoryId(new CategoryId(entity.getCategoryId()))
			.imageUrl(entity.getImageUrl())
			.embedding(new EmbeddingVector(entity.getEmbedding()))
			.keywords(entity.getKeywords())
			.viewCount(entity.getViewCount())
			.createdAt(entity.getCreatedAt())
			.build();

		// 변환된 도메인 객체의 keywords 값 확인
		log.debug("Domain keywords: {}", image.getKeywords());
		return image;
	}
}