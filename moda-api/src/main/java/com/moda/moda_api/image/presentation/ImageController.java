package com.moda.moda_api.image.presentation;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moda.moda_api.common.annotation.UserId;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.image.application.response.ImageListResponse;
import com.moda.moda_api.image.application.service.ImageService;
import com.moda.moda_api.image.application.service.ImageValidService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/image")
public class ImageController {
	private final ImageService imageService;
	private final ImageValidService imageValidService;

	@PostMapping
	public ResponseEntity<Boolean> createImages(
		@UserId String userId,
		@RequestPart("files") List<MultipartFile> files) {
		try {
			if (files.isEmpty()) {
				return ResponseEntity.badRequest()
					.body(false);
			}

			// 각 파일 검증
			for (MultipartFile file : files) {
				if (imageValidService.validateFile(file)) {
					return ResponseEntity.badRequest()
						.body(false);
				}
			}

			return ResponseEntity.ok(imageService.createImages(userId, files));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(false);
		}
	}


	@GetMapping("")
	public ResponseEntity<SliceResponseDto<ImageListResponse>> getImageList(
		@UserId String userId,
		@RequestParam Long categoryId,
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "15") Integer size,
		@RequestParam(defaultValue = "createdAt") String sortBy,
		@RequestParam(defaultValue = "DESC") String sortDirection
	) {
		SliceResponseDto<ImageListResponse> responseList = imageService.getImageList(
			userId, categoryId, page, size, sortBy, sortDirection
		);
		return ResponseEntity.ok(responseList);
	}

	@DeleteMapping("/{imageIds}")
	public ResponseEntity<Boolean> deleteImage(
		@UserId String userId,
		@PathVariable String imageIds
	) {
		Boolean result = imageService.deleteImage(userId, imageIds);
		return ResponseEntity.ok(result);
	}

}
