package com.moda.moda_api.user.presentation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청을 위한 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
public class LoginRequest {
    private String useremail;
    private String password;
}