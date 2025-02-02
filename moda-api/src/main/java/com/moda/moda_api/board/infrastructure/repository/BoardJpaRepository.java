package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.infrastructure.entity.BoardEntity;
import com.moda.moda_api.board.infrastructure.entity.BoardWithCardsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, String> {
    Optional<BoardEntity> findTopByUserIdOrderByPositionDesc(String userId);

    boolean existsByBoardIdAndUserId(String boardId, String userId);

    List<BoardEntity> findByUserIdOrderByPosition(String userId);

    boolean existsByUserIdAndBoardIdIn(String userId, Set<String> boardIds);

    @Query(value = """
        SELECT b as board,
               c as cards
        FROM boards b
        LEFT JOIN LATERAL (
            SELECT c2.*
            FROM cards c2
            WHERE c2.board_id = b.board_id
            AND c2.deleted_at IS NULL
            ORDER BY c2.created_at DESC
            LIMIT :cardLimit
        ) c ON true
        WHERE b.user_id = :userId
        ORDER BY b.position
        """,
        nativeQuery = true)
    List<BoardWithCardsProjection> findByUserIdWithRecentCards(
            @Param("userId") String userId,
            @Param("cardLimit") int cardLimit
    );
}
