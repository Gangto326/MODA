package com.moda.moda_api.image.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.image.domain.Image;
import com.moda.moda_api.image.domain.ImageId;
import com.moda.moda_api.image.infrastructure.entity.ImageEntity;
import com.moda.moda_api.user.domain.UserId;

@Component
public class ImageEntityMapper {
	public ImageEntity toEntity(Image image) {
		return ImageEntity.builder()
			.imageId(image.getImageId().getValue())
			.userId(image.getUserId().getValue())
			.categoryId(image.getCategoryId().getValue())
			.image_url(image.getImageUrl())
			.embedding(image.getEmbedding().getValues())
			.keywords(image.getKeywords())
			.viewCount(image.getViewCount())
			.createdAt(image.getCreatedAt())
			.build();
	}

	public Image toDomain(ImageEntity entity) {
		return Image.builder()
			.imageId(new ImageId(entity.getImageId()))
			.userId(new UserId(entity.getUserId()))
			.categoryId(new CategoryId(entity.getCategoryId()))
			.imageUrl(entity.getImage_url())
			.embedding(new EmbeddingVector(entity.getEmbedding()))
			.keywords(entity.getKeywords())
			.viewCount(entity.getViewCount())
			.createdAt(entity.getCreatedAt())
			.build();
	}
}