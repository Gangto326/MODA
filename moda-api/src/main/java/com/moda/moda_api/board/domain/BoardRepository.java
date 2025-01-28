package com.moda.moda_api.board.domain;

import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Integer> findLastPosition(String userId);
}
