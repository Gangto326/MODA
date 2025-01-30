package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.domain.BoardBookmark;
import com.moda.moda_api.board.infrastructure.entity.BoardBookmarkEntity;
import com.moda.moda_api.board.infrastructure.entity.BoardBookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardBookmarkJpaRepository extends JpaRepository<BoardBookmarkEntity, BoardBookmarkId> {
    Optional<Integer> findTopByUserIdOrderByPositionDesc(String userId);

    boolean existsByUserIdAndBoardId(String userId, String boardId);

    void deleteByUserIdAndBoardId(String userId, String boardId);

    List<BoardBookmarkEntity> findByUserIdOrderByPositionAsc(String userId);
}
