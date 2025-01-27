package com.moda.moda_api.user.presentation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청을 위한 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
}
