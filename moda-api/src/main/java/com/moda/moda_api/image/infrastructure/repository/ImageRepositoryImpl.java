package com.moda.moda_api.image.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.image.domain.Image;
import com.moda.moda_api.image.domain.ImageId;
import com.moda.moda_api.image.domain.repository.ImageRepository;
import com.moda.moda_api.image.infrastructure.entity.ImageEntity;
import com.moda.moda_api.image.infrastructure.mapper.ImageEntityMapper;
import com.moda.moda_api.user.domain.UserId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ImageRepositoryImpl implements ImageRepository {
	private final ImageJpaRepository imageJpaRepository;
	private final ImageEntityMapper imageEntityMapper;

	@Override
	public Image save(Image image) {
		ImageEntity entity = imageEntityMapper.toEntity(image);
		ImageEntity savedEntity = imageJpaRepository.save(entity);
		return imageEntityMapper.toDomain(savedEntity);
	}

	@Override
	public Slice<Image> findByUserIdAndCategoryId(UserId userId, CategoryId categoryId, Pageable pageable) {
		Slice<ImageEntity> imageEntities = imageJpaRepository.findByUserIdAndCategoryId(userId.getValue(),categoryId.getValue(),pageable);
		return imageEntities.map(imageEntityMapper::toDomain);
	}

	@Override
	public Optional<Image> findByUserIdAndImageId(UserId userId, ImageId imageId) {
		return imageJpaRepository.findByUserIdAndImageId(userId.getValue(), imageId.getValue())
			.map(imageEntityMapper::toDomain);
	}

	@Override
	public boolean delete(Image image) {
		ImageEntity entity = imageEntityMapper.toEntity(image);
		imageJpaRepository.delete(entity);
		return true;
	}

	@Override
	public boolean deleteAll(List<Image> imagesToDelete) {
		List<ImageEntity> imageEntities = imagesToDelete.stream()
			.map(imageEntityMapper::toEntity)
			.collect(Collectors.toList());

		imageJpaRepository.deleteAll(imageEntities);
		return true;
	}

	@Override
	public void saveAll(List<Image> images) {
		List<ImageEntity> imageEntities = images.stream()
			.map(imageEntityMapper::toEntity)
			.collect(Collectors.toList());

		imageJpaRepository.saveAll(imageEntities);
	}
}
