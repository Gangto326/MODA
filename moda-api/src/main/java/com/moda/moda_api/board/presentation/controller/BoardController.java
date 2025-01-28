package com.moda.moda_api.board.presentation.controller;

import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardPositionRequest;
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
            @RequestBody CreateBoardRequest request
    ) {
        BoardResponse boardResponse = boardService.createBoard(userId, request);
        return ResponseEntity.ok(boardResponse);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<List<BoardResponse>> deleteBoard(
            String userId,
            @PathVariable String boardId
    ) {
        List<BoardResponse> responseList = boardService.deleteBoard(userId, boardId);
        return ResponseEntity.ok(responseList);
    }

    @PatchMapping("/position")
    public ResponseEntity<List<BoardResponse>> updateBoardPosition(
            String userId,
            @RequestBody UpdateBoardPositionRequest request
    ) {
        List<BoardResponse> responseList = boardService.updateBoardPosition(userId, request);
        return null;
    }
}
