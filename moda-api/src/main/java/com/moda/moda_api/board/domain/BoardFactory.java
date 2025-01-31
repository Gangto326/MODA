package com.moda.moda_api.board.domain;

import org.springframework.stereotype.Component;
import com.moda.moda_api.user.domain.UserId;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 새로운 Board 생성을 위한 Domain
 */
@Component
public class BoardFactory {
    public Board create(UserId userId, String title, Position position, Boolean isPublic) {
        Board.validateTitle(title);
        return Board.builder()
                .boardId(generateBoardId())
                .userId(userId)
                .title(title)
                .position(position)
                .isPublic(isPublic)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Board의 ID를 생성. common의 UUID 메서드 사용 예정
    private BoardId generateBoardId() {
        return new BoardId(UUID.randomUUID().toString());
    }
}
