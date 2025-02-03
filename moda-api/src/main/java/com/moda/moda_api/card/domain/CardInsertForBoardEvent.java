package com.moda.moda_api.card.domain;

import com.moda.moda_api.board.domain.BoardId;
import lombok.Getter;

/**
 * Card에 따른 Board의 상태 변화를 이벤트 형식으로 전송합니다.
 */
@Getter
public class CardInsertForBoardEvent {
    private final String boardId;

    private CardInsertForBoardEvent(String boardId) {
        this.boardId = boardId;
    }

    public static CardInsertForBoardEvent from(BoardId boardId) {
        return new CardInsertForBoardEvent(boardId.getValue());
    }
}
