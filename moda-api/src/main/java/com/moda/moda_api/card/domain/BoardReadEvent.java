package com.moda.moda_api.card.domain;

import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.user.domain.UserId;
import lombok.Getter;

@Getter
public class BoardReadEvent {
    private final UserId userId;
    private final BoardId boardId;

    private BoardReadEvent(UserId userId, BoardId boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }

    public static BoardReadEvent from(UserId userId, BoardId boardId) {
        return new BoardReadEvent(userId, boardId);
    }
}
