package com.moda.moda_api.card.domain;

import com.moda.moda_api.board.exception.InvalidBoardIdException;
import lombok.Value;

@Value
public class CardId {
    String value;

    public CardId(String value) {
        validateCardId(value);
        this.value = value;
    }

    /**
     * CardId 값 검증
     */
    private void validateCardId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidBoardIdException("카드 ID가 존재하지 않습니다.");
        }
    }
}
