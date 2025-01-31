package com.moda.moda_api.card.application.service;

import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardFactory;
import com.moda.moda_api.card.domain.CardRepository;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardFactory cardFactory;

    @Transactional
    public Boolean createCard(String userId, String url) {
        UserId userIdObj = new UserId(userId);

        // url로 AI API 메서드 호출

        // 임베딩 메서드 호출

        Card card = cardFactory.create(userIdObj,
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null);

        Card savedCard = cardRepository.save(card);
        return true;
    }
}
