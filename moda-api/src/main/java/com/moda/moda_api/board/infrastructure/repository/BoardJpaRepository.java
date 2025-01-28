package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.domain.Board;
import com.moda.moda_api.board.infrastructure.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, String> {
    Optional<Integer> findTopByOrderByPositionDesc();

    boolean existsByBoardIdAndUserId(String boardId, String userId);

    List<BoardEntity> findByUserIdOrderByPosition(String userId);
}
