package com.moda.moda_api.board.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardBookmarkResponse {
    private String boardId;
    private String userId;
    private String ownerId;
    private String title;
    private Integer position;
    private boolean isBookmarked;
    private LocalDateTime createdAt;
}
