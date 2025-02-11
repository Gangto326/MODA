package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CardJpaRepository extends JpaRepository<CardEntity, String> {
    Slice<CardEntity> findByUserId(String value, Pageable pageable);

    Slice<CardEntity> findByUserIdAndCategoryId(String userId, Long categoryId, Pageable pageable);

    Optional<CardEntity> findByUserIdAndCardId(String userId, String cardId);

    @Query("SELECT c FROM CardEntity c WHERE c.urlCache.urlHash = :urlHash")
    Optional<CardEntity> findFirstByUrlHash(@Param("urlHash") String urlHash);

    List<CardEntity> findByUserIdAndTypeIdInAndDeletedAtIsNull(String userId, List<Integer> typeIds, Pageable pageable);

    @Query("SELECT c FROM CardEntity c WHERE c.userId = :userId AND c.typeId IN :typeIds " +
            "AND c.createdAt BETWEEN :startDate AND :endDate AND c.deletedAt IS NULL " +
            "ORDER BY FUNCTION('RANDOM')")
    List<CardEntity> findRandomCards(
            @Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("typeIds") List<Integer> typeIds,
            Pageable toDaysPage);

    List<CardEntity> findByUserIdAndViewCountAndTypeIdIn(String value, Integer viewCount, List<Integer> typeIds, Pageable pageable);
}
