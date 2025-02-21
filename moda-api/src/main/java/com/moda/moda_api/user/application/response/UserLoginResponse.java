package com.moda.moda_api.user.application.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponse {
	// 사용자 기본 정보
	private String email;        // 이메일
	private String nickname;     // 닉네임
	private String userName;     // 사용자 이름

	// 토큰 정보
	private String accessToken;  // 액세스 토큰
	private String refreshToken; // 리프레시 토큰
}