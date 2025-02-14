package com.moda.moda_api.common.jwt;

import com.moda.moda_api.user.domain.UserId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

	private UserId userId;
	private String tokenType;
	private String tokenValue;
	private long issuedAt;
	private long expiresAt;

}
