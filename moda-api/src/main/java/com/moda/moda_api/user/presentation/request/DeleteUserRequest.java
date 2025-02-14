package com.moda.moda_api.user.presentation.request;

import lombok.*;

/**
 * 회원 탈퇴 요청을 위한 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DeleteUserRequest {
    private String email;
    private String password;
    private String verificationCode;
}
