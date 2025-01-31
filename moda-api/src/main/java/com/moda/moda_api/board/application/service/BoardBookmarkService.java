package com.moda.moda_api.board.application.service;

import com.moda.moda_api.board.application.mapper.BoardBookmarkDtoMapper;
import com.moda.moda_api.board.application.response.ToggleBookmarkResponse;
import com.moda.moda_api.board.domain.*;
import com.moda.moda_api.board.exception.BoardNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardBookmarkService {
    private final BoardRepository boardRepository;
    private final BoardBookmarkRepository boardBookmarkRepository;
    private final BoardBookmarkFactory boardBookmarkFactory;
    private final BoardBookmarkDtoMapper boardBookmarkDtoMapper;
    private final BoardPositionService boardPositionService;


    /**
     * 북마크 토글을 위한 메서드
     * @param userId
     * @param boardId
     * @return
     */
    @Transactional
    public ToggleBookmarkResponse toggleBookmark(String userId, String boardId) {
        UserId userIdObj = new UserId(userId);
        BoardId boardIdObj = new BoardId(boardId);

        // 보드 존재 여부 확인
        Board board = boardRepository.findByBoardId(boardIdObj.getValue())
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 보드입니다."));

        // 이미 즐겨찾기한 보드인지 확인
        if (boardBookmarkRepository.existsByUserIdAndBoardId(userIdObj.getValue(), boardIdObj.getValue())) {
            // 값이 있으면 즐겨찾기 취소이므로 삭제
            boardBookmarkRepository.deleteByUserIdAndBoardId(userIdObj.getValue(), boardIdObj.getValue());
            // false 반환
            return boardBookmarkDtoMapper.toResponse(boardIdObj, false);
        }
        
        // 새로운 즐겨찾기 보드의 INSERT를 위한 위치 탐색
        Position position = getNextBookmarkPosition(userIdObj);
        
        // 즐겨찾기 생성 후 저장
        boardBookmarkRepository.save(
                boardBookmarkFactory.create(board, userIdObj, position)
        );

        // true 반환
        return boardBookmarkDtoMapper.toResponse(boardIdObj, true);
    }

    /**
     * lastPosition이 1000이 아니면 마지막의 다음 position 생성 후 반환.
     * 1000을 넘으면 user의 모든 즐겨찾기 보드를 재정렬 (Lazy 처리)
     * @param userId
     * @return
     */
    private Position getNextBookmarkPosition(UserId userId) {
        Optional<Integer> lastPosition = boardBookmarkRepository.findLastPosition(userId.getValue());
        
        // 마지막 position이 1000 이상이면 user의 모드 즐겨찾기 보드 리스트를, 아니면 빈 리스트를 가짐
        List<BoardBookmark> bookmarks = lastPosition.isPresent() && lastPosition.get() >= 1000
                ? boardBookmarkRepository.findByUserIdOrderByPosition(userId.getValue())
                : Collections.emptyList();
        return boardPositionService.getNextPosition(lastPosition, bookmarks);
    }
}
