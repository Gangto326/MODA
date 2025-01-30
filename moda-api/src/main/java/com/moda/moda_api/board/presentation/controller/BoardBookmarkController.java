package com.moda.moda_api.board.presentation.controller;

import com.moda.moda_api.board.application.response.ToggleBookmarkResponse;
import com.moda.moda_api.board.application.service.BoardBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BoardBookmarkController {
    private final BoardBookmarkService boardBookmarkService;

    /**
     * 보드 즐겨찾기 추가/삭제 토글
     * @param boardId
     * @return
     */
    @PostMapping("/{boardId}")
    public ResponseEntity<ToggleBookmarkResponse> toggleBookmark(
            String userId,
            @PathVariable String boardId
    ) {
        ToggleBookmarkResponse toggleBookmarkResponse = boardBookmarkService.toggleBookmark(userId, boardId);
        return ResponseEntity.ok(toggleBookmarkResponse);
    }
}
