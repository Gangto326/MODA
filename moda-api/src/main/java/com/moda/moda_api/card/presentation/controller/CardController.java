package com.moda.moda_api.card.presentation.controller;

import java.util.concurrent.CompletableFuture;

import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.application.service.CardService;
import com.moda.moda_api.card.presentation.request.CardRequest;
import com.moda.moda_api.card.presentation.request.MoveCardRequest;
import com.moda.moda_api.card.presentation.request.UpdateCardRequest;
import com.moda.moda_api.common.annotation.UserId;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/card")
public class CardController {
    private final CardService cardService;

    // @PostMapping("/test")
    // public CompletableFuture<ResponseEntity<Boolean>> createCardIfKnowRequestID(@RequestBody CardRequest cardRequest){
    //     return cardService.createCardByRequestId(cardRequest.getUrl(), cardRequest.getRequestId())
    //         .thenApply(ResponseEntity::ok);
    // };

    @PostMapping("")
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
    ){
        CardDetailResponse response = cardService.updateCardContent(userId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/board")
    public ResponseEntity<Boolean> updateCardBoard(
            @UserId String userId,
            @RequestBody MoveCardRequest request
    ){
        Boolean result = cardService.updateCardBoard(userId, request);
        return ResponseEntity.ok(result);
    }
}
