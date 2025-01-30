package com.moda.moda_api.board.domain;

import java.util.List;
import java.util.Optional;

public interface BoardBookmarkRepository {
    boolean existsByUserIdAndBoardId(String userId, String boardId);

    void deleteByUserIdAndBoardId(String userId, String boardId);

    Optional<Integer> findLastPosition(String userId);

    BoardBookmark save(BoardBookmark boardBookmark);

    List<BoardBookmark> findByUserIdOrderByPosition(String value);
}
