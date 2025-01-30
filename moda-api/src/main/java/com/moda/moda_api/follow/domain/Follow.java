package com.moda.moda_api.follow.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 팔로우 도메인 모델입니다.
 * 사용자 간의 팔로우 관계를 표현하는 도메인 객체입니다.
 * JPA나 다른 기술에 의존하지 않는 순수한 도메인 객체입니다.
 */
@Getter
public class Follow {
    private String followId;
    private String followerId;  // 팔로우를 하는 사용자 ID
    private String followingId; // 팔로우를 받는 사용자 ID
    private LocalDateTime followedAt;

    // 팔로워 프로필 정보
    private String followerNickname;
    private String followerProfileImage;

    // 팔로잉 프로필 정보
    private String followingNickname;
    private String followingProfileImage;

    /**
     * 빌더 패턴을 사용한 Follow 객체 생성을 위한 생성자입니다.
     * followedAt은 객체 생성 시점에 자동으로 설정됩니다.
     */
    @Builder
    public Follow(String followId, String followerId, String followingId,
                  String followerNickname, String followerProfileImage,
                  String followingNickname, String followingProfileImage) {
        this.followId = followId;
        this.followerId = followerId;
        this.followingId = followingId;
        this.followerNickname = followerNickname;
        this.followerProfileImage = followerProfileImage;
        this.followingNickname = followingNickname;
        this.followingProfileImage = followingProfileImage;
        this.followedAt = LocalDateTime.now();
    }

    /**
     * 특정 사용자가 팔로워인지 확인합니다.
     */
    public boolean isFollower(String userId) {
        return this.followerId.equals(userId);
    }

    /**
     * 특정 사용자가 팔로잉 대상인지 확인합니다.
     */
    public boolean isFollowing(String userId) {
        return this.followingId.equals(userId);
    }

    /**
     * 팔로우 관계가 특정 두 사용자 사이의 것인지 확인합니다.
     */
    public boolean isFollowBetween(String followerId, String followingId) {
        return this.followerId.equals(followerId) && this.followingId.equals(followingId);
    }
}