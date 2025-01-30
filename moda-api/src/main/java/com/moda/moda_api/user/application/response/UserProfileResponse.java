package com.moda.moda_api.user.application.response;

import com.moda.moda_api.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자 프로필 상세 정보를 반환하기 위한 DTO 클래스입니다.
 * 프로필 조회 시 사용됩니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserProfileResponse {
    private String userId;
    private String email;
    private String nickname;
    private String profileImage;
    private String status;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}
