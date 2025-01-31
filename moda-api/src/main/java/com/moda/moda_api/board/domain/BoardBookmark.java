package com.moda.moda_api.board.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardBookmark implements Positionable {
    private Board board;
    private UserId userId;
    private Position position;

    public BoardId getBoardId() {
        return board.getId();
    }

    public UserId getBoardOwnerId() {
        return board.getUserId();
    }

    public String getTitle() {
        return board.getTitle();
    }

    public LocalDateTime getBoardCreatedAt() {
        return board.getCreatedAt();
    }

    @Override
    public void movePosition(Position position) {
        this.position = position;
    }
}
