package com.moda.moda_api.board.domain;


import com.moda.moda_api.board.exception.InvalidBookmarkException;
import com.moda.moda_api.board.exception.InvalidPositionException;
import com.moda.moda_api.board.exception.InvalidTitleException;
import com.moda.moda_api.board.exception.UnauthorizedException;
import com.moda.moda_api.user.domain.UserId;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board implements Positionable {
    private BoardId boardId;
    private UserId userId;
    private String title;
    private Position position;
    private boolean isPublic;
    private LocalDateTime createdAt;


    public void changeTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    public void toggleVisibillity() {
        this.isPublic = !this.isPublic;
    }

    public boolean isMine(UserId userId) {
        return this.userId.equals(userId);
    }

    @Override
    public void movePosition(Position position) {
        this.position = position;
    }

    public void movePosition(Position position, Position maxPosition) {
        validatePositionRange(position, maxPosition);
        this.position = position;
    }

    private void validatePositionRange(Position position, Position maxPosition) {
        if (position.getValue() > maxPosition.getValue()) {
            throw new InvalidPositionException("Position은 보드의 최대 개수를 초과할 수 없습니다.");
        }
    }

    public void validateCurrentPosition(Position sourcePosition) {
        if (!this.position.equals(sourcePosition)) {
            throw new InvalidPositionException("현재 보드의 위치가 일치하지 않습니다. 현재 위치: " + this.position.getValue() + ", 요청 위치: " + sourcePosition.getValue());
        }
    }

    protected static void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidTitleException("보드명은 빈 문자열일 수 없습니다.");
        }
        if (title.length() > 10) {
            throw new InvalidTitleException("보드명은 10자 이하여야 합니다.");
        }
    }

    public void validateBookmarkable() {
        if (!this.isPublic) {
            throw new InvalidBookmarkException("비공개 보드는 즐겨찾기할 수 없습니다.");
        }
    }

    public void validateOwnership(UserId userId) {
        if (!this.userId.equals(userId)) {
            throw new UnauthorizedException("권한이 존재하지 않습니다.");
        }
    }
}
