package com.moda.moda_api.board.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToggleBookmarkResponse {
    String boardId;
    private boolean isBookmarked;
}
