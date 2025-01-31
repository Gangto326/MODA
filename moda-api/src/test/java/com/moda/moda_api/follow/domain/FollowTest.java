package com.moda.moda_api.follow.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FollowTest {

    private Follow follow;
    private String followerId;
    private String followingId;

    @BeforeEach
    void setUp() {
        // Given
        followerId = "user-1";
        followingId = "user-2";
        follow = Follow.builder()
                .followId(UUID.randomUUID().toString())
                .followerId(followerId)
                .followingId(followingId)
                .followerNickname("팔로워")
                .followerProfileImage("follower.jpg")
                .followingNickname("팔로잉")
                .followingProfileImage("following.jpg")
                .build();
    }

    // 주어진 사용자가 팔로워인지 확인할 수 있다
    @Test
    void isFollowerTest() {
        // When & Then
        assertThat(follow.isFollower(followerId)).isTrue();
        assertThat(follow.isFollower(followingId)).isFalse();
        assertThat(follow.isFollower("other-user")).isFalse();
    }

    // 주어진 사용자가 팔로잉 대상인지 확인할 수 있다
    @Test
    void isFollowingTest() {
        // When & Then
        assertThat(follow.isFollowing(followingId)).isTrue();
        assertThat(follow.isFollowing(followerId)).isFalse();
        assertThat(follow.isFollowing("other-user")).isFalse();
    }

    // 주어진 두 사용자 간의 팔로우 관계인지 확인할 수 있다
    @Test
    void isFollowBetweenTest() {
        // When & Then
        assertThat(follow.isFollowBetween(followerId, followingId)).isTrue();
        assertThat(follow.isFollowBetween(followingId, followerId)).isFalse();
        assertThat(follow.isFollowBetween(followerId, "other-user")).isFalse();
        assertThat(follow.isFollowBetween("other-user", followingId)).isFalse();
    }

    // 팔로워의 프로필 정보를 가져올 수 있다
    @Test
    void followerProfileTest() {
        // When & Then
        assertThat(follow.getFollowerNickname()).isEqualTo("팔로워");
        assertThat(follow.getFollowerProfileImage()).isEqualTo("follower.jpg");
    }

    // 팔로잉 대상의 프로필 정보를 가져올 수 있다
    @Test
    void followingProfileTest() {
        // When & Then
        assertThat(follow.getFollowingNickname()).isEqualTo("팔로잉");
        assertThat(follow.getFollowingProfileImage()).isEqualTo("following.jpg");
    }

    // 팔로우 생성 시점이 자동으로 설정된다
    @Test
    void followedAtTest() {
        // When & Then
        assertThat(follow.getFollowedAt()).isNotNull();
    }
}