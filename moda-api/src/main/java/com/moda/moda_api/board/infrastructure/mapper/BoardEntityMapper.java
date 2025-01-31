package com.moda.moda_api.board.infrastructure.mapper;

import com.moda.moda_api.board.domain.Board;
import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.board.domain.Position;
import com.moda.moda_api.user.domain.UserId;
import com.moda.moda_api.board.infrastructure.entity.BoardEntity;
import org.springframework.stereotype.Component;

@Component
public class BoardEntityMapper {

    public BoardEntity toEntity(Board board) {
        return BoardEntity.builder()
                .boardId(board.getBoardId().getValue())
                .userId(board.getUserId().getValue())
                .title(board.getTitle())
                .isPublic(board.isPublic())
                .position(board.getPosition().getValue())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public Board toDomain(BoardEntity entity) {
        return Board.builder()
                .boardId(new BoardId(entity.getBoardId()))
                .userId(new UserId(entity.getUserId()))
                .title(entity.getTitle())
                .isPublic(entity.isPublic())
                .position(new Position(entity.getPosition()))
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
