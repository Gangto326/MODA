package com.moda.moda_api.image.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moda.moda_api.image.infrastructure.entity.ImageEntity;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, String> {
	Slice<ImageEntity> findByUserIdAndCategoryId(String userId, Long categoryId, Pageable pageable);
	Optional<ImageEntity> findByUserIdAndImageId(String userId, String imageId);
}
