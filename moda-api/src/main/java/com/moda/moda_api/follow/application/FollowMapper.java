package com.moda.moda_api.follow.application;

import com.moda.moda_api.follow.domain.Follow;
import com.moda.moda_api.follow.application.response.FollowResponse;
import com.moda.moda_api.follow.application.response.FollowerResponse;
import com.moda.moda_api.follow.application.response.FollowingResponse;
import org.springframework.stereotype.Component;

/**
 * Follow 도메인 객체와 DTO 간의 변환을 담당하는 매퍼 클래스입니다.
 * 도메인 객체와 DTO 간의 변환 책임을 중앙화하여 관리합니다.
 */
@Component
public class FollowMapper {

    /**
     * Follow 도메인 객체를 기본 FollowResponse DTO로 변환합니다.
     */
    public FollowResponse toFollowResponse(Follow follow) {
        return FollowResponse.builder()
                .followId(follow.getFollowId())
                .followerId(follow.getFollowerId())
                .followingId(follow.getFollowingId())
                .followedAt(follow.getFollowedAt())
                .build();
    }

    /**
     * Follow 도메인 객체를 FollowerResponse DTO로 변환합니다.
     * 팔로워의 프로필 정보를 포함합니다.
     */
    public FollowerResponse toFollowerResponse(Follow follow) {
        return FollowerResponse.builder()
                .followId(follow.getFollowId())
                .followerId(follow.getFollowerId())
                .followerNickname(follow.getFollowerNickname())
                .followerProfileImage(follow.getFollowerProfileImage())
                .followedAt(follow.getFollowedAt())
                .build();
    }

    /**
     * Follow 도메인 객체를 FollowingResponse DTO로 변환합니다.
     * 팔로잉하는 사용자의 프로필 정보를 포함합니다.
     */
    public FollowingResponse toFollowingResponse(Follow follow) {
        return FollowingResponse.builder()
                .followId(follow.getFollowId())
                .followingId(follow.getFollowingId())
                .followingNickname(follow.getFollowingNickname())
                .followingProfileImage(follow.getFollowingProfileImage())
                .followedAt(follow.getFollowedAt())
                .build();
    }
}