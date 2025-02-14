package com.moda.moda_api.follow.application.response;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 팔로잉 정보를 반환하기 위한 응답(Response) DTO 클래스입니다.
 * 팔로잉하는 사용자의 상세 정보(프로필 등)를 포함합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FollowingResponse {
    private String followId;
    private String followingId;
    private String followingNickname;
    private String followingProfileImage;
    private LocalDateTime followedAt;
}