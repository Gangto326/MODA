package com.moda.moda_api.image.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.image.domain.Image;
import com.moda.moda_api.image.domain.ImageId;
import com.moda.moda_api.user.domain.UserId;

public interface ImageRepository {
	Image save(Image image);

	Slice<Image> findByUserIdAndCategoryId(UserId userId, CategoryId categoryId, Pageable pageable);

	Optional<Image> findByUserIdAndImageId(UserId userId, ImageId imageId);

	boolean delete(Image image);

	boolean deleteAll(List<Image> imagesToDelete);

	void saveAll(List<Image> images);

}
