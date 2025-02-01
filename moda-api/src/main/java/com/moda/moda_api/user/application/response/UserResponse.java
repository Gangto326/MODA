package com.moda.moda_api.user.application.response;

import com.moda.moda_api.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자 기본 정보를 반환하기 위한 응답(Response) DTO 클래스입니다.
 * 주로 로그인, 회원가입 완료 후 응답으로 사용됩니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    private String userId;
    private String email;
    private String nickname;
    private String profileImage;
    private String role;
    private LocalDateTime createdAt;
}
