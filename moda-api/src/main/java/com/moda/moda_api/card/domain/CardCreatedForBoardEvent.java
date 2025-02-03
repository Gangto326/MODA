package com.moda.moda_api.card.domain;

import lombok.Getter;

/**
 * Card에 따른 Board의 상태 변화를 이벤트 형식으로 전송합니다.
 */
@Getter
public class CardCreatedForBoardEvent {
    private final String boardId;

    private CardCreatedForBoardEvent(String boardId) {
        this.boardId = boardId;
    }

    public static CardCreatedForBoardEvent from(Card card) {
        return new CardCreatedForBoardEvent(card.getBoardId().getValue());
    }
}
