package com.moda.moda_api.board.presentation.controller;

import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public BoardResponse createBoard(
            String userId,
            @RequestBody CreateBoardRequest createBoardRequest
    ) {
        return boardService.createBoard(userId, createBoardRequest);
    }
}
