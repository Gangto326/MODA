package com.moda.moda_api.board.domain;

import com.moda.moda_api.user.domain.UserId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BoardRepository {
    Board save(Board board);

    Optional<Integer> findLastPosition(String userId);

    boolean delete(Board board);

    boolean existsByBoardIdAndUserId(String boardId, String userId);

    Optional<Board> findByBoardId(String boardId);

    List<Board> findByUserIdOrderByPosition(String userId);

    void saveAll(List<Board> boardList);

    boolean existsByUserIdAndBoardIdIn(String userId, Set<String> boardIds);

    boolean deleteAll(List<Board> boardsToDelete);

    List<BoardWithCards> findByUserIdWithRecentCards(String userId, int cardLimit);
}
