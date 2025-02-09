package com.moda.moda_api.image.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.common.infrastructure.ImageStorageService;
import com.moda.moda_api.common.pagination.SliceRequestDto;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.image.application.exception.ImageNotFoundException;
import com.moda.moda_api.image.application.response.ImageListResponse;
import com.moda.moda_api.image.domain.Image;
import com.moda.moda_api.image.domain.ImageId;
import com.moda.moda_api.image.domain.repository.ImageRepository;
import com.moda.moda_api.summary.infrastructure.api.PythonAiClient;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.user.domain.UserId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ImageService {
	private final ImageRepository imageRepository;
	private final ImageStorageService imageStorageService;
	private final PythonAiClient pythonAiClient;

	@Transactional
	public Boolean createImages(String userId, List<MultipartFile> multipartFiles) {
		UserId userIdObj = new UserId(userId);

		List<Image> images = multipartFiles.stream()
			.map(file -> {
				try {

					// 실제 AI 분석
					// AIAnalysisResponseDTO aiAnalysisResponseDTO = pythonAiClient.imageAnalysis();

					// 테스트용 AI 분석 (실제 구현 시에는 pythonAiClient.imageAnalysis 사용)
					AIAnalysisResponseDTO aiAnalysisResponseDTO = AIAnalysisResponseDTO.builder()
						.categoryId(new CategoryId(1L))
						.embeddingVector(new EmbeddingVector())
						.keywords(new String[] {"핵심", "이미지", "그게뭔데"})
						.build();

					String s3Url = imageStorageService.uploadMultipartFile(file);

					// Image 엔티티 생성
					return Image.builder()
						.userId(userIdObj)
						.imageUrl(s3Url)
						.categoryId(aiAnalysisResponseDTO.getCategoryId())
						.embedding(aiAnalysisResponseDTO.getEmbeddingVector())
						.keywords(aiAnalysisResponseDTO.getKeywords())
						.build();
				} catch (Exception e) {
					throw new RuntimeException("Failed to process image", e);
				}
			})
			.collect(Collectors.toList());

		imageRepository.saveAll(images);

		return true;
	}

	public SliceResponseDto<ImageListResponse> getImageList(
		String userId, Long categoryId, Integer page, Integer size, String sortBy, String sortDirection
	) {
		UserId userIdObj = new UserId(userId);
		CategoryId categoryIdObj = new CategoryId(categoryId);

		// Slice 값 생성
		SliceRequestDto sliceRequestDto = SliceRequestDto.builder()
			.page(page)
			.size(size)
			.sortBy(sortBy)
			.sortDirection(sortDirection)
			.build();

		// Slice와 파라미터 조건에 맞는 카드를 가져옵니다.
		Slice<Image> images = imageRepository.findByUserIdAndCategoryId(
			userIdObj,
			categoryIdObj,
			sliceRequestDto.toPageable()
		);

		// 페이지네이션 메타 데이터와 함께 반환합니다.
		return SliceResponseDto.of(
			images.map(image -> ImageListResponse.builder()
				.imageId(image.getImageId().getValue())
				.imageUrl(image.getImageUrl())
				.categoryId(image.getCategoryId().getValue())
				.build()
			)
		);
	}

	@Transactional
	public Boolean deleteImage(String userId, String imageIds) {
		UserId userIdObj = new UserId(userId);

		List<ImageId> imageIdList = toImageIds(Arrays.asList(imageIds.split(",")));

		List<Image> imagesToDelete = findImageList(userIdObj, imageIdList);

		imagesToDelete.forEach(image -> image.validateOwnership(userIdObj));

		return imageRepository.deleteAll(imagesToDelete);
	}

	private List<ImageId> toImageIds(List<String> imageIds) {
		return imageIds.stream()
			.map(ImageId::new)
			.collect(Collectors.toList());
	}

	private Image findImage(UserId userId, ImageId imageId) {
		return imageRepository.findByUserIdAndImageId(userId, imageId)
			.orElseThrow(() -> new ImageNotFoundException("카드를 찾을 수 없습니다."));
	}

	private List<Image> findImageList(UserId userId, List<ImageId> imageLists) {
		return imageLists.stream()
			.map(imageId -> findImage(userId, imageId))
			.collect(Collectors.toList());
	}
}
