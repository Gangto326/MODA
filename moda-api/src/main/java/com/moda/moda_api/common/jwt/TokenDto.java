package com.moda.moda_api.common.jwt;

import com.moda.moda_api.user.domain.UserId;

import lombok.Builder;

@Builder
public class TokenDto {

	private UserId userId;
	private TokenType tokenType;
	private String token;
	private Long issuedAt;
	private Long expiresAt;

}
