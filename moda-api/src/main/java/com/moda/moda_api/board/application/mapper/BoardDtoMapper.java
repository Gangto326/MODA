package com.moda.moda_api.board.application.mapper;

import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.domain.Board;
import org.springframework.stereotype.Component;

@Component
public class BoardDtoMapper {
    // Domain객체를 Response객체로 매핑
    public BoardResponse toResponse(Board board) {
        return BoardResponse.builder()
                .boardId(board.getId().getValue())
                .userId(board.getUserId().getValue())
                .title(board.getTitle())
                .isPublic(board.isPublic())
                .position(board.getPosition().getValue())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
