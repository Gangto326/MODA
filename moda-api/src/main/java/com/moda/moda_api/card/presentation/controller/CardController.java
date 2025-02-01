package com.moda.moda_api.card.presentation.controller;

import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.service.CardService;
import com.moda.moda_api.card.presentation.request.MoveCardRequest;
import com.moda.moda_api.card.presentation.request.UpdateCardRequest;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
public class CardController {
    private final CardService cardService;

    @PostMapping("")
    public ResponseEntity<Boolean> createCard(
            String userId,
            String url
    ) {
        Boolean result = cardService.createCard(userId, url);
        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    public ResponseEntity<SliceResponseDto<CardDetailResponse>> getCardList(
            String userId,
            String boardId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "15") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        SliceResponseDto<CardDetailResponse> responseList = cardService.getCardDetailList(
                userId, boardId, page, size, sortBy, sortDirection
        );
        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{cardIds}")
    public ResponseEntity<Boolean> deleteCard(
            String userId,
            @PathVariable String cardIds
    ) {
        Boolean result = cardService.deleteCard(userId, cardIds);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("")
    public ResponseEntity<CardDetailResponse> updateCardContent(
            String userId,
            UpdateCardRequest request
    ){
        CardDetailResponse response = cardService.updateCardContent(userId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/board")
    public ResponseEntity<Boolean> updateCardBoard(
            String userId,
            MoveCardRequest request
    ){
        Boolean result = cardService.updateCardBoard(userId, request);
        return ResponseEntity.ok(result);
    }
}
