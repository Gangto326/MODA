package com.moda.moda_api.board.application.service;

import com.moda.moda_api.board.application.mapper.BoardDtoMapper;
import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.domain.*;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardFactory boardFactory;
    private final BoardRepository boardRepository;
    private final BoardDtoMapper boardDtoMapper;

    /**
     * 새로운 Board를 생성합니다.
     * @param userId
     * @param request
     * @return 생성된 Board의 정보
     */
    @Transactional
    public BoardResponse createBoard(String userId, CreateBoardRequest request) {
        UserId userIdObj = new UserId(userId);

        // user의 마지막 Board의 position을 가져와서 다음 Position 생성
        Position position = boardRepository.findLastPosition(userId)
                .map(Position::of)
                .map(Position::next)
                .orElseGet(Position::first);

        // isPublic의 값에 따라 Board 생성
        Board board = boardFactory.create(
                userIdObj,
                request.getTitle(),
                position,
                request.getIsPublic());

        // DB에 INSERT 후 값 반환
        Board savedBoard = boardRepository.save(board);

        // Domain 객체를 Response로 매핑 후 반환
        return boardDtoMapper.toResponse(savedBoard);
    }
}
