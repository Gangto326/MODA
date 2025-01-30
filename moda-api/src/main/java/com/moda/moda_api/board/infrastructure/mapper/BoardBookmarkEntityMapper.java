package com.moda.moda_api.board.infrastructure.mapper;

import com.moda.moda_api.board.domain.Board;
import com.moda.moda_api.board.domain.BoardBookmark;
import com.moda.moda_api.board.domain.Position;
import com.moda.moda_api.board.domain.UserId;
import com.moda.moda_api.board.infrastructure.entity.BoardBookmarkEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardBookmarkEntityMapper {
    private final BoardEntityMapper boardEntityMapper;

    public BoardBookmarkEntity toEntity(BoardBookmark boardBookmark) {
        return BoardBookmarkEntity.builder()
                .boardId(boardBookmark.getBoardId().getValue())
                .userId(boardBookmark.getUserId().getValue())
                .position(boardBookmark.getPosition().getValue())
                .build();
    }

    public BoardBookmark toDomain(BoardBookmarkEntity savedEntity) {
        return BoardBookmark.builder()
                .board(boardEntityMapper.toDomain(savedEntity.getBoard()))
                .userId(new UserId(savedEntity.getUserId()))
                .position(new Position(savedEntity.getPosition()))
                .build();
    }
}
