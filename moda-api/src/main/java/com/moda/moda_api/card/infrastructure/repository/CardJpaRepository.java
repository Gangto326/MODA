package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardJpaRepository extends JpaRepository<CardEntity, String> {
    Slice<CardEntity> findByBoardUserIdAndBoardId(String userId, String boardId, Pageable pageable);

    @Query("SELECT c FROM CardEntity c WHERE c.boardId = :boardId AND c.deletedAt IS NULL ORDER BY c.createdAt DESC")
    List<CardEntity> findThreeCardsByBoardIdOrderByCreatedAtDesc(@Param("boardId") String boardId, Pageable pageable);

    @Query("SELECT c FROM CardEntity c WHERE c.urlCache.urlHash = :urlHash")
    Optional<CardEntity> findFirstByUrlHash(@Param("urlHash") String urlHash);
}
