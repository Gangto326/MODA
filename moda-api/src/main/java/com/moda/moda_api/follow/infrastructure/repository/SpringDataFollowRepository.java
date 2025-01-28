package com.moda.moda_api.follow.infrastructure.repository;

import com.moda.moda_api.follow.infrastructure.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA를 사용한 Follow 엔티티 레포지토리입니다.
 */
public interface SpringDataFollowRepository extends JpaRepository<FollowEntity, String> {

    // 특정 사용자의 팔로워 목록 조회 (프로필 정보 포함)
    @Query("SELECT f FROM FollowEntity f " +
            "JOIN UserEntity u ON f.followerId = u.id " +
            "WHERE f.followingId = :userId")
    List<FollowEntity> findFollowersWithProfile(@Param("userId") String userId);

    // 특정 사용자의 팔로잉 목록 조회 (프로필 정보 포함)
    @Query("SELECT f FROM FollowEntity f " +
            "JOIN UserEntity u ON f.followingId = u.id " +
            "WHERE f.followerId = :userId")
    List<FollowEntity> findFollowingsWithProfile(@Param("userId") String userId);

    // 특정 사용자의 팔로워 수 조회
    long countByFollowingId(String followingId);

    // 특정 사용자의 팔로잉 수 조회
    long countByFollowerId(String followerId);

    // 특정 팔로우 관계 조회
    FollowEntity findByFollowerIdAndFollowingId(String followerId, String followingId);

    // 특정 사용자의 팔로워 중 키워드로 검색
    @Query("SELECT f FROM FollowEntity f " +
            "JOIN UserEntity u ON f.followerId = u.id " +
            "WHERE f.followingId = :userId " +
            "AND u.nickname LIKE %:keyword%")
    List<FollowEntity> searchFollowersByKeyword(
            @Param("userId") String userId,
            @Param("keyword") String keyword
    );

    // 특정 사용자의 팔로잉 중 키워드로 검색
    @Query("SELECT f FROM FollowEntity f " +
            "JOIN UserEntity u ON f.followingId = u.id " +
            "WHERE f.followerId = :userId " +
            "AND u.nickname LIKE %:keyword%")
    List<FollowEntity> searchFollowingsByKeyword(
            @Param("userId") String userId,
            @Param("keyword") String keyword
    );
}