package com.moda.moda_api.board.domain;

import com.moda.moda_api.card.domain.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardWithCards {
    private final Board board;
    private final List<Card> recentCards;
    private final Boolean isRead;

    // 읽음 상태를 변경한 새로운 객체를 반환
    public BoardWithCards withReadStatus(Boolean isRead) {
        return new BoardWithCards(this.board, this.recentCards, isRead);
    }
}
