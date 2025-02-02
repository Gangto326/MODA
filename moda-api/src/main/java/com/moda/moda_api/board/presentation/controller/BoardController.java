package com.moda.moda_api.board.presentation.controller;

import com.moda.moda_api.board.application.response.BoardListResponse;
import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardPositionRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardTitleRequest;
import com.moda.moda_api.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
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
            @UserId String userId,
            @RequestBody CreateBoardRequest request
    ) {
        log.info("createBoard가 호출되었습니다.: "+userId+" "+request.getTitle()+" "+request.getIsPublic());
        BoardResponse boardResponse = boardService.createBoard(userId, request);
        log.info("createBoard가 완료되었습니다.");
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
            @UserId String userId,
            @PathVariable String boardIds
    ) {
        List<BoardResponse> responseList = boardService.deleteBoard(userId, boardIds);
        return ResponseEntity.ok(responseList);
    }

    /**
     * 보드 위치 변경
     * sourcePosition은 변경할 보드의 Position이어야 합니다.
     * targetPosition은 변경될 위치에 존재하는 보드의 Position이어야 합니다.
     * @param userId
     * @param request
     * @return
     */
    @PatchMapping("/position")
    public ResponseEntity<List<BoardResponse>> updateBoardPosition(
            @UserId String userId,
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
            @UserId String userId,
            @RequestBody UpdateBoardTitleRequest request
    ) {
        BoardResponse boardResponse = boardService.updateBoardTitle(userId, request);
        return ResponseEntity.ok(boardResponse);
    }

    /**
     * 보드 리스트 반환
     * @param userId
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<BoardListResponse>> getBoardList(
            @UserId String userId
    ) {
        List<BoardListResponse> responseList = boardService.getBoardList(userId);
        return ResponseEntity.ok(responseList);
    }
}
