package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.exception.InvalidTitleException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    private BoardId id;
    private UserId userId;
    private String title;
    private Position position;
    private boolean isPublic;
    private LocalDateTime createdAt;


    public void changeTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    public void movePosition(Position position) {
        this.position = position;
    }

    public void toggleVisibillity() {
        this.isPublic = !this.isPublic;
    }

    public boolean isMine(UserId userId) {
        return this.userId.equals(userId);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidTitleException("보드명은 빈 문자열일 수 없습니다.");
        }
        if (title.length() > 100) {
            throw new InvalidTitleException("보드명은 100자 이하여야 합니다.");
        }
    }
}
