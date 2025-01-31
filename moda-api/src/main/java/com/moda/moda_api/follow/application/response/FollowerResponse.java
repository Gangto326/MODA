package com.moda.moda_api.follow.application.response;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 팔로워 정보를 반환하기 위한 응답(Response) DTO 클래스입니다.
 * 팔로워의 상세 정보(프로필 등)를 포함합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FollowerResponse {
    private String followId;
    private String followerId;
    private String followerNickname;
    private String followerProfileImage;
    private LocalDateTime followedAt;
}