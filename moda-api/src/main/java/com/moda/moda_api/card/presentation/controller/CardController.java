package com.moda.moda_api.card.presentation.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.moda.moda_api.card.application.response.CardMainResponse;
import com.moda.moda_api.card.presentation.request.CardBookmarkRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.application.service.CardService;
import com.moda.moda_api.card.application.service.ImageValidService;
import com.moda.moda_api.card.presentation.request.CardRequest;
import com.moda.moda_api.card.presentation.request.MoveCardRequest;
import com.moda.moda_api.card.presentation.request.UpdateCardRequest;
import com.moda.moda_api.common.annotation.UserId;
import com.moda.moda_api.common.pagination.SliceResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/card")
public class CardController {
	private final CardService cardService;
	private final ImageValidService imageValidService;

	// Json으로 날라올 때
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public CompletableFuture<ResponseEntity<Boolean>> createCard(
		@UserId String userId,
		@RequestBody CardRequest cardRequest
	) {
		return cardService.createCard(userId, cardRequest.getUrl())
			.thenApply(ResponseEntity::ok)
			.exceptionally(throwable -> {
				log.error("Failed to create card", throwable);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
			});
	}

	//MediaType으로 날라올 때
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CompletableFuture<ResponseEntity<Boolean>> createCard(
		@UserId String userId,
		@RequestPart("files") List<MultipartFile> files) {
		if (files.isEmpty()) {
			return CompletableFuture.completedFuture(
				ResponseEntity.badRequest().body(false)
			);
		}

		// 각 파일 검증
		for (MultipartFile file : files) {
			if (imageValidService.validateFile(file)) {
				return CompletableFuture.completedFuture(
					ResponseEntity.badRequest().body(false)
				);
			}
		}

		return CompletableFuture.supplyAsync(() -> {
			try {
				boolean result = cardService.createImages(userId, files);
				return ResponseEntity.ok(result);
			} catch (Exception e) {
				log.error("Failed to create images", e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
			}
		});
	}

	@GetMapping("")
	public ResponseEntity<SliceResponseDto<CardListResponse>> getCardList(
		@UserId String userId,
		@RequestParam Long categoryId,
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "15") Integer size,
		@RequestParam(defaultValue = "createdAt") String sortBy,
		@RequestParam(defaultValue = "DESC") String sortDirection
	) {
		SliceResponseDto<CardListResponse> responseList = cardService.getCardList(
			userId, categoryId, page, size, sortBy, sortDirection
		);
		return ResponseEntity.ok(responseList);
	}

	@GetMapping("/{cardId}")
	public ResponseEntity<CardDetailResponse> getCardDetail(
		@UserId String userId,
		@PathVariable String cardId
	) {
		CardDetailResponse response = cardService.getCardDetail(userId, cardId);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{cardIds}")
	public ResponseEntity<Boolean> deleteCard(
		@UserId String userId,
		@PathVariable String cardIds
	) {
		Boolean result = cardService.deleteCard(userId, cardIds);
		return ResponseEntity.ok(result);
	}

	@PatchMapping("")
	public ResponseEntity<CardDetailResponse> updateCardContent(
		@UserId String userId,
		@RequestBody UpdateCardRequest request
	) {
		CardDetailResponse response = cardService.updateCardContent(userId, request);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/board")
	public ResponseEntity<Boolean> updateCardBoard(
		@UserId String userId,
		@RequestBody MoveCardRequest request
	) {
		Boolean result = cardService.updateCardBoard(userId, request);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/bookmark")
	public ResponseEntity<Boolean> cardBookmark(
			@UserId String userId,
			@RequestBody CardBookmarkRequest request
	) {
		Boolean result = cardService.cardBookmark(userId, request);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/main")
	public ResponseEntity<CardMainResponse> getMainKeywords(
			@UserId String userId
	) {
		CardMainResponse response = cardService.getMainKeywords(userId);
		return ResponseEntity.ok(response);
	}
}
