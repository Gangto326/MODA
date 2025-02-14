package com.moda.moda_api.follow.domain;

import java.util.List;

/**
 * 팔로우 도메인을 위한 리포지토리 인터페이스입니다.
 * 실제 구현체(JpaFollowRepository)와 도메인 모델 사이의 추상화 계층입니다.
 * 특정 기술(JPA 등)에 의존하지 않는 순수한 인터페이스입니다.
 */
public interface FollowRepository {
    /**
     * 팔로우 관계를 저장합니다.
     *
     * @param follow 저장할 팔로우 엔티티
     * @return 저장된 팔로우 엔티티
    //  */
    // Follow save(Follow follow);
    //
    // /**
    //  * ID로 팔로우 관계를 조회합니다.
    //  *
    //  * @param followId 조회할 팔로우 관계의 ID
    //  * @return 조회된 팔로우 관계, 없으면 null
    //  */
    // Follow findById(String followId);
    //
    // /**
    //  * 특정 사용자의 팔로워 목록을 조회합니다.
    //  *
    //  * @param userId 팔로워를 조회할 사용자의 ID
    //  * @return 해당 사용자를 팔로우하는 팔로워 목록
    //  */
    // List<Follow> findAllFollowersByUserId(String userId);
    //
    // /**
    //  * 특정 사용자의 팔로잉 목록을 조회합니다.
    //  *
    //  * @param userId 팔로잉을 조회할 사용자의 ID
    //  * @return 해당 사용자가 팔로우하는 사용자 목록
    //  */
    // List<Follow> findAllFollowingsByUserId(String userId);
    //
    // /**
    //  * 두 사용자 간의 팔로우 관계를 조회합니다.
    //  *
    //  * @param followerId 팔로워 ID
    //  * @param followingId 팔로잉 ID
    //  * @return 존재하는 팔로우 관계, 없으면 null
    //  */
    // Follow findByFollowerIdAndFollowingId(String followerId, String followingId);
    //
    // /**
    //  * 팔로우 관계를 삭제합니다.
    //  *
    //  * @param followId 삭제할 팔로우 관계의 ID
    //  */
    // void delete(String followId);
    //
    // /**
    //  * 특정 사용자의 팔로워 수를 조회합니다.
    //  *
    //  * @param userId 팔로워 수를 조회할 사용자의 ID
    //  * @return 해당 사용자의 팔로워 수
    //  */
    // long countFollowersByUserId(String userId);
    //
    // /**
    //  * 특정 사용자의 팔로잉 수를 조회합니다.
    //  *
    //  * @param userId 팔로잉 수를 조회할 사용자의 ID
    //  * @return 해당 사용자의 팔로잉 수
    //  */
    // long countFollowingsByUserId(String userId);
    //
    // /**
    //  * 특정 사용자의 팔로워 중 키워드로 검색합니다.
    //  *
    //  * @param userId 팔로워를 검색할 사용자의 ID
    //  * @param keyword 검색 키워드 (닉네임)
    //  * @return 검색된 팔로워 목록
    //  */
    // List<Follow> searchFollowersByKeyword(String userId, String keyword);
    //
    // /**
    //  * 특정 사용자의 팔로잉 중 키워드로 검색합니다.
    //  *
    //  * @param userId 팔로잉을 검색할 사용자의 ID
    //  * @param keyword 검색 키워드 (닉네임)
    //  * @return 검색된 팔로잉 목록
    //  */
    // List<Follow> searchFollowingsByKeyword(String userId, String keyword);
}