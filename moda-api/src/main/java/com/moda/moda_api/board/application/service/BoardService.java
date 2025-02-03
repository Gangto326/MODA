package com.moda.moda_api.board.application.service;

import com.moda.moda_api.board.application.mapper.BoardDtoMapper;
import com.moda.moda_api.board.application.response.BoardListResponse;
import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.domain.*;
import com.moda.moda_api.board.exception.UnauthorizedException;
import com.moda.moda_api.card.domain.CardRepository;
import com.moda.moda_api.user.domain.UserId;
import com.moda.moda_api.board.exception.BoardNotFoundException;
import com.moda.moda_api.board.presentation.request.CreateBoardRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardPositionRequest;
import com.moda.moda_api.board.presentation.request.UpdateBoardTitleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardFactory boardFactory;
    private final BoardRepository boardRepository;
    private final BoardDtoMapper boardDtoMapper;
    private final BoardPositionService boardPositionService;
    private final ReadBoardRepository readBoardRepository;
    private final CardRepository cardRepository;

    /**
     * 새로운 Board를 생성합니다.
     * @param userId
     * @param request
     * @return 생성된 Board의 정보
     */
    @Transactional
    public BoardResponse createBoard(String userId, CreateBoardRequest request) {
        UserId userIdObj = new UserId(userId);

        // 새로운 보드의 INSERT를 위한 위치 탐색
        Position position = getNextBoardPosition(userIdObj);

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
     * @param boardIds
     * @return
     */
    @Transactional
    public List<BoardResponse> deleteBoard(String userId, String boardIds) {
        UserId userIdObj = new UserId(userId);
        
        // ,로 구분된 boardId들을 Value Object로 매핑하며 id 검증
        List<BoardId> boardIdList = Arrays.stream(boardIds.split(","))
                .map(boardId -> new BoardId(boardId))
                .collect(Collectors.toList());
        
        // 삭제할 보드들 조회 및 권한 검증
        List<Board> boardsToDelete = boardIdList.stream()
                .map(boardId -> {
                    Board board = boardRepository.findByBoardId(boardId.getValue())
                            .orElseThrow(() -> new BoardNotFoundException("보드를 찾을 수 없습니다: " + boardId.getValue()));
                    board.validateOwnership(userIdObj);
                    return board;
                })
                .collect(Collectors.toList());

        // Board 삭제
        boardRepository.deleteAll(boardsToDelete);

        // 해당 User의 모든 보드를 position 정렬에 맞게 조회
        List<Board> afterBoardList = boardRepository.findByUserIdOrderByPosition(userIdObj.getValue());

        return afterBoardList.stream()
                .map(boardDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * User의 권한을 확인하고 Board의 Position을 변경합니다.
     * 변경되는 보드의 범위에 따라 범위 내의 보드의 값을 +1 또는 -1 합니다.
     *
     * return 값은 position을 기준으로 정렬됩니다.
     * @param userId
     * @param request targetPosition은 변경될 위치에 존재하는 보드의 Position이어야 합니다.
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

        // 해당 User의 모든 보드를 조회
        List<Board> boardList = boardRepository.findByUserIdOrderByPosition(userIdObj.getValue());

        // 위치를 변경할 보드 가져오기
        Board targetBoard = boardList.stream()
                .filter(board -> board.getBoardId().equals(boardIdObj))
                .findFirst()
                .orElseThrow(() -> new BoardNotFoundException("보드를 찾을 수 없습니다"));

        // 보드 내의 가장 큰 Position 탐색 -> targetPosition은 해당 Position보다 클 수 없음
        Position maxPosition = findMaxPosition(boardList);

        // 권한 확인
        targetBoard.validateOwnership(userIdObj);
        // 위치 확인
        targetBoard.validateCurrentPosition(sourcePosition);

        log.info("위치 변경 로직 전 - targetBoard: {}", targetBoard.getPosition().getValue());
        // 변경되는 범위의 보드 위치 변경
        adjustPositions(boardList, sourcePosition, targetPosition);
        log.info("위치 변경 로직 후 - targetBoard: {}", targetBoard.getPosition().getValue());

        // 타겟 보드의 위치 변경 후 저장
        targetBoard.movePosition(targetPosition, maxPosition);
        boardRepository.save(targetBoard);
        log.info("타겟 보드의 위치 할당 후 - targetBoard: {}", targetBoard.getPosition().getValue());
        
        // Position에 맞게 정렬 후 BoardResponse로 변경하여 반환
        return boardPositionService.sortByPosition(boardList).stream()
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

        // 이름을 변경할 보드 가져오기
        Board board = boardRepository.findByBoardId(boardIdObj.getValue())
                .orElseThrow(() -> new BoardNotFoundException("보드를 찾을 수 없습니다"));

        // 권한 확인
        board.validateOwnership(userIdObj);

        // 보드 이름 변경 후 저장
        log.info("이름 변경 전 - boardTitle: {}", board.getTitle());
        board.changeTitle(request.getTitle());
        boardRepository.save(board);
        log.info("이름 변경 후 - boardTitle: {}", board.getTitle());
        
        return boardDtoMapper.toResponse(board);
    }

    /**
     * 해당 유저의 보드 리스트를 반환합니다.
     * 각 보드에는 가장 최신 Card가 최대 세 장 들어있습니다.
     * @param userId
     * @return
     */
    public List<BoardListResponse> getBoardList(String userId) {
        UserId userIdObj = new UserId(userId);

        // 보드 리스트 조회
        List<Board> boards = boardRepository.findByUserIdOrderByPosition(userIdObj.getValue());

        // 읽음 상태 조회
        List<BoardId> readBoardIds = readBoardRepository.findReadBoardIds(userIdObj);

        // 각 보드 리스트에 읽음 상태 반영
        List<BoardWithCards> boardsWithReadStatus = getBoardWithCardsWithReadStatus(boards, readBoardIds);

        return boardsWithReadStatus.stream()
                .map(boardDtoMapper::toBoardListResponse)
                .collect(Collectors.toList());
    }

    /**
     * 보드 리스트를 위한 모든 보드별 읽음 여부 상태를 반영합니다.
     * @param boards
     * @param readBoardIds
     * @return
     */
    private List<BoardWithCards> getBoardWithCardsWithReadStatus(List<Board> boards, List<BoardId> readBoardIds) {
        return boards.stream()
                .map(board -> new BoardWithCards(
                        board,
                        cardRepository.findCardsByBoardIdOrderByCreatedAtDesc(board.getBoardId(), 3),
                        readBoardIds.contains(board.getBoardId())
                ))
                .collect(Collectors.toList());
    }

    /**
     * lastPosition이 1000이 아니면 마지막의 다음 position 생성 후 반환.
     * 1000을 넘으면 user의 모든 즐겨찾기 보드를 재정렬 (Lazy 처리)
     * @param userId
     * @return
     */
    private Position getNextBoardPosition(UserId userId) {
        Optional<Integer> lastPosition = boardRepository.findLastPosition(userId.getValue());

        // 마지막 position이 1000 이상이면 user의 모드 즐겨찾기 보드 리스트를, 아니면 빈 리스트를 가짐
        List<Board> boards = lastPosition.isPresent() && lastPosition.get() >= 1000
                ? boardRepository.findByUserIdOrderByPosition(userId.getValue())
                : Collections.emptyList();
        return boardPositionService.getNextPosition(lastPosition, boards);
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
        boardPositionService.adjustPositions(boardList, source, target);

        // 보드 저장
        boardRepository.saveAll(boardList);
    }

    /**
     * 여러 보드의 User 권한을 동시에 검사하는 메서드
     * @param userId
     * @param boardIds
     */
    private void validateOwnership(UserId userId, Set<BoardId> boardIds) {
        if (!boardRepository.existsByUserIdAndBoardIdIn(
                userId.getValue(),
                boardIds.stream().map(BoardId::getValue)
                        .collect(Collectors.toSet()))) {
            throw new UnauthorizedException("권한이 존재하지 않습니다.");
        }
    }

    /**
     * 입력으로 들어오는 보드의 리스트 중 가장 큰 Position을 반환합니다.
     * @param boards
     * @return
     */
    private Position findMaxPosition(List<Board> boards) {
        return boards.stream()
                .map(Board::getPosition)
                .max(Comparator.comparing(Position::getValue))
                .orElse(Position.first());
    }

    /**
     * Card 도메인에서 보드의 권한 확인을 사용할 때 활용
     * @param userId
     * @param boardIds
     */
    public void someOtherBoardOperation(UserId userId, Set<BoardId> boardIds) {
        validateOwnership(userId, boardIds);
    }

    /**
     * ReadBoard 테이블에서 해당 보드를 읽었다는 데이터 삽입
     * 해당 보드를 읽은 상태가 됩니다.
     * @param userId
     * @param boardId
     */
    @Transactional
    public void markBoardReadStatus(UserId userId, BoardId boardId) {
        log.info("markBoardReadStatus 메서드 호출");
        readBoardRepository.save(userId, boardId);
    }

    /**
     * ReadBoard 테이블에서 해당 보드를 읽었다는 데이터 삭제
     * 모든 유저가 해당 보드를 읽지 않은 상태가 됩니다.
     * @param boardId
     */
    @Transactional
    public void resetBoardReadStatus(BoardId boardId) {
        readBoardRepository.deleteByBoardId(boardId);
    }
}
