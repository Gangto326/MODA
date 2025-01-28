package com.moda.moda_api.board.application.service;

import com.moda.moda_api.board.application.mapper.BoardDtoMapper;
import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.domain.*;
import com.moda.moda_api.board.exception.BoardNotFoundException;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardPositionRequest;
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

        // 포지션 재조정 후 전체 리스트 반환
        return adjustPositions(userIdObj, board.getPosition(), Position.max())
                .stream().map(boardDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * private 메소드로 선언하여 별도의 트랜잭션을 생성하지 않음
     * User개인 보드 리스트 중 position 이후의 모든 보드의 순서를 -1 합니다.
     * @param userId
     * @param source
     * @param target
     * @return 재정렬된 보드 리스트를 반환
     */
    private List<Board> adjustPositions(UserId userId, Position source, Position target) {
        // 해당 User의 모든 보드를 조회
        List<Board> boardList = boardRepository.findByUserIdOrderByPosition(userId.getValue());
        
        // 움직이는 범위 내의 보드들의 position 값 변경
        Position.adjustPositions(boardList, source, target);

        // 보드 저장
        boardRepository.saveAll(boardList);

        // 변경된 전체 보드 리스트 반환
        return boardList;
    }

    /**
     * User의 권한을 확인하고 Board의 Position을 변경합니다.
     * 변경되는 보드의 범위에 따라 범위 내의 보드의 값을 +1 또는 -1 합니다.
     *
     * return 값이 position을 기준으로 정렬되지 않은 값임에 유의합니다.
     * ex) before: 1, 2, 3, 4, 5
     *     request: source = 1, target = 3
     *     adjustPositions(source, target): 1, 1, 2, 4, 5
     *     targetBoard.movePosition(targetPosition): 3, 1, 2, 4, 5
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

        // 변경되는 범위의 보드 위치 변경
        List<Board> boardList = adjustPositions(userIdObj, sourcePosition, targetPosition);

        // 타겟 보드의 위치 변경 후 JPA Dirty Checking
        targetBoard.movePosition(targetPosition);

        return boardList.stream()
                .map(boardDtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
