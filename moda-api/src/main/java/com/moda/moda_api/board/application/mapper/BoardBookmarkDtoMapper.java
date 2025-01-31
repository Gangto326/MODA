package com.moda.moda_api.board.application.mapper;

import com.moda.moda_api.board.application.response.ToggleBookmarkResponse;
import com.moda.moda_api.board.domain.BoardId;

public class BoardBookmarkDtoMapper {
//    public BoardBookmarkResponse toResponse(BoardBookmark boardBookmark) {
//
//    }

    public ToggleBookmarkResponse toResponse(BoardId boardId, Boolean isBookmarked) {
        return ToggleBookmarkResponse.builder()
                .boardId(boardId.getValue())
                .isBookmarked(isBookmarked)
                .build();
    }
}
