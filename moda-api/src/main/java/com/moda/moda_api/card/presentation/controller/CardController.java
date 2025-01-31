package com.moda.moda_api.card.presentation.controller;

import com.moda.moda_api.card.application.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
