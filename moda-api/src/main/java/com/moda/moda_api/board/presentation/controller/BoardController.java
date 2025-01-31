package com.moda.moda_api.board.presentation.controller;

import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardPositionRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardTitleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    /**
     * 보드 생성
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseEntity<BoardResponse> createBoard(
            String userId,
            @RequestBody CreateBoardRequest request
    ) {
        BoardResponse boardResponse = boardService.createBoard(userId, request);
        return ResponseEntity.ok(boardResponse);
    }

    /**
     * 보드 삭제
     * @param userId
     * @param boardIds
     * @return
     */
    @DeleteMapping("/{boardIds}")
    public ResponseEntity<List<BoardResponse>> deleteBoard(
            String userId,
            @PathVariable String boardIds
    ) {
        List<BoardResponse> responseList = boardService.deleteBoard(userId, boardIds);
        return ResponseEntity.ok(responseList);
    }

    /**
     * 보드 위치 변경
     * @param userId
     * @param request
     * @return
     */
    @PatchMapping("/position")
    public ResponseEntity<List<BoardResponse>> updateBoardPosition(
            String userId,
            @RequestBody UpdateBoardPositionRequest request
    ) {
        List<BoardResponse> responseList = boardService.updateBoardPosition(userId, request);
        return ResponseEntity.ok(responseList);
    }

    /**
     * 보드 제목 변경
     * @param userId
     * @param request
     * @return
     */
    @PatchMapping("/title")
    public ResponseEntity<BoardResponse> updateBoardTitle(
            String userId,
            @RequestBody UpdateBoardTitleRequest request
    ) {
        BoardResponse boardResponse = boardService.updateBoardTitle(userId, request);
        return ResponseEntity.ok(boardResponse);
    }
}
