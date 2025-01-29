package com.moda.moda_api.board.application.service;

import com.moda.moda_api.board.application.mapper.BoardDtoMapper;
import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.domain.*;
import com.moda.moda_api.board.exception.BoardNotFoundException;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardPositionRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardTitleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<BoardResponse> deleteBoard(String userId, String boardId) {
        UserId userIdObj = new UserId(userId);
        BoardId boardIdObj = new BoardId(boardId);
        
        // 권한 확인
        authorizationService.isBoardOwner(userIdObj, boardIdObj);

        // 삭제 보드 가져오기
        Board board = boardRepository.findByBoardId(boardIdObj.getValue())
                .orElseThrow(() -> new BoardNotFoundException("보드를 찾을 수 없습니다"));

        // Board 삭제
        boardRepository.delete(board);

        // 해당 User의 모든 보드를 position 정렬에 맞게 조회
        List<Board> boardList = boardRepository.findByUserIdOrderByPosition(userIdObj.getValue());

        // 포지션 재조정 후 전체 리스트 반환
        adjustPositions(boardList, board.getPosition(), Position.max());

        return boardList.stream()
                .map(boardDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * private 메소드로 선언하여 별도의 트랜잭션을 생성하지 않음
     * 보드 리스트를 조건에 맞게 정렬합니다.
     * @param boardList
     * @param source
     * @param target
     */
    private void adjustPositions(List<Board> boardList, Position source, Position target) {
        // 움직이는 범위 내의 보드들의 position 값 변경
        Position.adjustPositions(boardList, source, target);

        // 보드 저장
        boardRepository.saveAll(boardList);
    }

    /**
     * User의 권한을 확인하고 Board의 Position을 변경합니다.
     * 변경되는 보드의 범위에 따라 범위 내의 보드의 값을 +1 또는 -1 합니다.
     *
     * return 값은 position을 기준으로 정렬됩니다.
     * @param userId
     * @param request
     * @return
     */
    @Transactional
    public List<BoardResponse> updateBoardPosition(
            String userId,
            UpdateBoardPositionRequest request
    ) {
        UserId userIdObj = new UserId(userId);
        BoardId boardIdObj = new BoardId(request.getBoardId());
        Position sourcePosition = new Position(request.getSourcePosition());
        Position targetPosition = new Position(request.getTargetPosition());

        // 권한 확인
        authorizationService.isBoardOwner(userIdObj, boardIdObj);

        // 위치를 변경할 보드 가져오기
        Board targetBoard = boardRepository.findByBoardId(boardIdObj.getValue())
                .orElseThrow(() -> new BoardNotFoundException("보드를 찾을 수 없습니다"));

        // 해당 User의 모든 보드를 조회
        List<Board> boardList = boardRepository.findByUserIdOrderByPosition(userIdObj.getValue());

        // 변경되는 범위의 보드 위치 변경
        adjustPositions(boardList, sourcePosition, targetPosition);

        // 타겟 보드의 위치 변경 후 JPA Dirty Checking
        targetBoard.movePosition(targetPosition);
        
        // Position에 맞게 정렬 후 BoardResponse로 변경하여 반환
        return Position.sortByPosition(boardList).stream()
                .map(boardDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 보드의 title을 변경합니다.
     * @param userId
     * @param request
     * @return
     */
    @Transactional
    public BoardResponse updateBoardTitle(String userId, UpdateBoardTitleRequest request) {
        UserId userIdObj = new UserId(userId);
        BoardId boardIdObj = new BoardId(request.getBoardId());

        // 권한 확인
        authorizationService.isBoardOwner(userIdObj, boardIdObj);

        // 이름을 변경할 보드 가져오기
        Board board = boardRepository.findByBoardId(boardIdObj.getValue())
                .orElseThrow(() -> new BoardNotFoundException("보드를 찾을 수 없습니다"));
        
        // 보드 이름 변경
        board.changeTitle(request.getTitle());
        
        return boardDtoMapper.toResponse(board);
    }
}
