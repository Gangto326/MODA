package com.moda.moda_api.board.application.service;

import com.moda.moda_api.board.application.mapper.BoardDtoMapper;
import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.domain.*;
import com.moda.moda_api.board.exception.BoardNotFoundException;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardFactory boardFactory;
    private final BoardRepository boardRepository;
    private final BoardDtoMapper boardDtoMapper;
    private final AuthorizationService authorizationService;

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

    /**
     * User의 권한을 확인하고 Board를 삭제합니다.
     * 삭제 후 해당 Board 이후의 모든 Board 위치를 한 칸씩 당깁니다.
     * @param userId
     * @param boardId
     * @return
     */
    @Transactional
    public List<Board> deleteBoard(String userId, String boardId) {
        UserId userIdObj = new UserId(userId);
        BoardId boardIdObj = new BoardId(boardId);
        
        // 권한 확인
        authorizationService.isBoardOwner(userIdObj, boardId);

        // 삭제 보드 가져오기
        Board board = boardRepository.findByBoardId(boardIdObj.getValue())
                .orElseThrow(() -> new BoardNotFoundException("보드를 찾을 수 없습니다"));

        // Board 삭제
        boardRepository.delete(board);

        // 포지션 재조정 후 전체 리스트 반환
        return decreasePositions(userIdObj, board.getPosition());
    }

    /**
     * private 메소드로 선언하여 별도의 트랜잭션을 생성하지 않음
     * User개인 보드 리스트 중 position 이후의 모든 보드의 순서를 -1 합니다.
     * @param userId
     * @param position
     * @return 재정렬된 보드 리스트를 반환
     */
    private List<Board> decreasePositions(UserId userId, Position position) {
        // 해당 User의 보드들 조회
        List<Board> boardList = boardRepository.findByUserIdOrderByPosition(userId.getValue(), position.getValue());

        // 삭제된 보드 position 이후의 보드들의 position -1
        Position.decreasePositions(boardList, position);

        // 보드 저장
        boardRepository.saveAll(boardList);

        // 변경된 전체 보드 리스트 반환
        return boardList;
    }
}
