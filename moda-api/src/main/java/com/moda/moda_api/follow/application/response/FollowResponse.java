package com.moda.moda_api.follow.application.response;

import lombok.*;
import java.time.LocalDateTime;

/**
 * 팔로우 관계의 기본 정보를 반환하기 위한 응답(Response) DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FollowResponse {
    private String followId;
    private String followerId;
    private String followingId;
    private LocalDateTime followedAt;
}