package com.moda.moda_api.follow.presentation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 팔로우 요청을 위한 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FollowRequest {
    private String followerId;    // 팔로우를 요청하는 사용자의 ID
    private String followingId;   // 팔로우 대상 사용자의 ID
}