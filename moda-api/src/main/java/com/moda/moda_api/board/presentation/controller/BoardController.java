package com.moda.moda_api.board.presentation.controller;

import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.board.domain.Board;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<BoardResponse> createBoard(
            String userId,
            @RequestBody CreateBoardRequest createBoardRequest
    ) {
        BoardResponse boardResponse = boardService.createBoard(userId, createBoardRequest);
        return ResponseEntity.ok(boardResponse);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<List<Board>> deleteBoard(
            String userId,
            @PathVariable String boardId
    ) {
        return ResponseEntity.ok(boardService.deleteBoard(userId, boardId));
    }
}
