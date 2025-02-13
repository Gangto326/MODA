package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.infrastructure.entity.CardDtoEntity;
import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CardJpaRepository extends JpaRepository<CardEntity, String> {
    Slice<CardEntity> findByUserId(String value, Pageable pageable);

    Slice<CardEntity> findByUserIdAndCategoryId(String userId, Long categoryId, Pageable pageable);

    @Query("SELECT c FROM CardEntity c LEFT JOIN FETCH c.urlCache WHERE c.userId = :userId AND c.cardId = :cardId")
    Optional<CardEntity> findByUserIdAndCardId(String userId, String cardId);

//    @Query("SELECT new com.moda.moda_api.card.dto.CardDTO(c, u.originalUrl) " +
//            "FROM CardEntity c " +
//            "LEFT JOIN c.urlCache u " +
//            "WHERE c.userId = :userId AND c.typeId IN :typeIds")
//    Optional<CardDtoEntity> findByUserIdAndCardId(@Param("userId") String userId, String cardId);

//    @Query("SELECT c FROM CardEntity c JOIN FETCH c.urlCache uc WHERE c.userId = :userId AND c.cardId = :cardId")
//    Optional<CardEntity> findByUserIdAndCardId(@Param("userId") String userId, @Param("cardId") String cardId);

    @Query("SELECT c FROM CardEntity c WHERE c.urlCache.urlHash = :urlHash")
    Optional<CardEntity> findFirstByUrlHash(@Param("urlHash") String urlHash);

//    @Query("SELECT c FROM CardEntity c WHERE c.userId = :userId AND c.typeId IN :typeIds " +
//            "AND c.createdAt BETWEEN :startDate AND :endDate AND c.deletedAt IS NULL " +
//            "ORDER BY FUNCTION('RANDOM')")
//    List<CardEntity> findRandomCards(
//            @Param("userId") String userId,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate,
//            @Param("typeIds") List<Integer> typeIds,
//            Pageable toDaysPage);

    @Query("SELECT new com.moda.moda_api.card.infrastructure.entity.CardDtoEntity(c, u.originalUrl) " +
            "FROM CardEntity c " +
            "LEFT JOIN c.urlCache u " +
            "WHERE c.userId = :userId AND c.typeId IN :typeIds " +
            "AND c.createdAt BETWEEN :startDate AND :endDate " +
            "AND c.deletedAt IS NULL " +
            "ORDER BY function('random')")
    List<CardDtoEntity> findRandomCards(
            @Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("typeIds") List<Integer> typeIds,
            Pageable pageable
    );

    //    List<CardEntity> findByUserIdAndViewCountAndTypeIdIn(String userId, Integer viewCount, List<Integer> typeIds, Pageable pageable);

    @Query("SELECT new com.moda.moda_api.card.infrastructure.entity.CardDtoEntity(c, u.originalUrl) " +
            "FROM CardEntity c " +
            "LEFT JOIN c.urlCache u " +
            "WHERE c.userId = :userId AND c.viewCount = :viewCount " +
            "AND c.typeId IN :typeIds AND c.deletedAt IS NULL")
    List<CardDtoEntity> findByUserIdAndViewCountAndTypeIdIn(
            @Param("userId") String userId,
            @Param("viewCount") Integer viewCount,
            @Param("typeIds") List<Integer> typeIds,
            Pageable pageable
    );

    //    List<CardEntity> findByUserIdAndTypeIdInAndDeletedAtIsNull(String userId, List<Integer> typeIds, Pageable pageable);

    @Query("SELECT new com.moda.moda_api.card.infrastructure.entity.CardDtoEntity(c, u.originalUrl) " +
            "FROM CardEntity c " +
            "LEFT JOIN c.urlCache u " +
            "WHERE c.userId = :userId AND c.typeId IN :typeIds AND c.deletedAt IS NULL")
    List<CardDtoEntity> findByUserIdAndTypeIdInAndDeletedAtIsNull(
            @Param("userId") String userId,
            @Param("typeIds") List<Integer> typeIds,
            Pageable pageable
    );

//    Slice<CardEntity> findByUserIdAndBookmarkTrueAndTypeIdAndDeletedAtIsNull(String userId, Integer typeId, Pageable pageable);

    @Query("SELECT new com.moda.moda_api.card.infrastructure.entity.CardDtoEntity(c, u.originalUrl) " +
            "FROM CardEntity c " +
            "LEFT JOIN c.urlCache u " +
            "WHERE c.userId = :userId AND c.bookmark = true AND c.typeId = :typeId AND c.deletedAt IS NULL")
    Slice<CardDtoEntity> findByUserIdAndBookmarkTrueAndTypeIdAndDeletedAtIsNull(
            @Param("userId") String userId,
            @Param("typeId") Integer typeId,
            Pageable pageable
    );

    List<CardEntity> findAllByTypeId(Integer typeId);

//    @Query("SELECT new com.moda.moda_api.card.infrastructure.entity.CardDtoEntity(c, u.originalUrl) " +
//            "FROM CardEntity c " +
//            "LEFT JOIN c.urlCache u " +
//            "WHERE c.typeId = :typeId")
//    List<CardDtoEntity> findAllByTypeId(@Param("typeId") Integer typeId);

    @Modifying
    @Query("UPDATE CardEntity c SET c.viewCount = c.viewCount + :increment WHERE c.cardId = :cardId")
    void updateViewCount(@Param("cardId") String cardId, @Param("increment") int increment);

    Boolean existsByUserIdAndUrlHashAndDeletedAtIsNull(String value, String urlHash);
}
