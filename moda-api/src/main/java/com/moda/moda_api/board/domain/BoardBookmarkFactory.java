package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.exception.SelfBookmarkNotAllowedException;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class BoardBookmarkFactory {
    public BoardBookmark create(Board board, UserId userId, Position position) {
        validateBookmarkCreation(board, userId);
        return BoardBookmark.builder()
                .board(board)
                .userId(userId)
                .position(position)
                .build();
    }

    private void validateBookmarkCreation(Board board, UserId userId) {
        // 비공개 보드인지 확인
        board.validateBookmarkable();
        
        if (board.getUserId().getValue().equals(userId.getValue())) {
            throw new SelfBookmarkNotAllowedException("내 보드는 즐겨찾기할 수 없습니다.");
        }
    }
}
