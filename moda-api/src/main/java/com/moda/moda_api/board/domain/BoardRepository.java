package com.moda.moda_api.board.domain;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);

    Optional<Integer> findLastPosition(String userId);

    boolean delete(Board board);

    boolean existsByBoardIdAndUserId(String boardId, String userId);

    Optional<Board> findByBoardId(String boardId);

    List<Board> findByUserIdOrderByPosition(String userId);

    void saveAll(List<Board> boardList);
}
