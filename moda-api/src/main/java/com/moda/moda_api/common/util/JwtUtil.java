package com.moda.moda_api.common.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import com.moda.moda_api.common.jwt.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.moda.moda_api.user.domain.User;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@NoArgsConstructor
public class JwtUtil {
	private final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 30; // 30초
	// private final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 15; // 15분

	private final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 6; // 6시간

	@Value("${jwt.secretkey.accesstoken}")
	private String accessTokenSecretKey;

	@Value("${jwt.secretkey.refreshtoken}")
	private String refreshTokenSecretKey;


	private SecretKey getSecretKey(String type) {
		// AccessToken과 RefreshToken을 서로 다른 키를 이용하여 발급.
		// AccessToken과 RefreshToken을 서로 변경하여 인증 시도를 방지.

		if(type.equals("AccessToken")) {
			return Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes());
		}

		else if(type.equals("RefreshToken")) {
			return Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes());
		}

		return null;
	}


	private long getExpiration(long issuedAt, String type) {

		if(type.equals("AccessToken")) {
			return issuedAt + ACCESS_TOKEN_EXPIRE_TIME;
		}

		else if(type.equals("RefreshToken")) {
			return issuedAt + REFRESH_TOKEN_EXPIRE_TIME;
		}

		return 0L;
	}


	public TokenDto generateToken(User user, String type) {

		// 발급 시간과 만료 시간을 계산.
		long issuedAt = System.currentTimeMillis();
		long expiresAt = getExpiration(issuedAt, type);

		String token = Jwts.builder()
				.claim("userId", user.getUserId().getValue())
				.issuedAt(new Date(issuedAt))
				.expiration(new Date(expiresAt))
				.signWith(getSecretKey(type))
				.compact();

		TokenDto tokenDto = TokenDto.builder()
				.userId(user.getUserId())
				.tokenType(type)
				.tokenValue(token)
				.issuedAt(issuedAt)
				.expiresAt(expiresAt)
				.build();

		return tokenDto;
	}


	public String getUserId(String token, String type) {

		if(isValidToken(token, type)) {

			Claims payload = Jwts.parser()
					.verifyWith(getSecretKey(type))
					.build()
					.parseSignedClaims(token)
					.getPayload();

			return payload.get("userId", String.class);
		}

		return null;
	}

	public boolean isValidToken(String token, String type) {
		Jws<Claims> payload = null;

		try {
			payload = Jwts.parser()
					.verifyWith(getSecretKey(type))
					.build()
					.parseSignedClaims(token);

			return true;
		} catch(ExpiredJwtException | SignatureException | MalformedJwtException e) {
			return false;
		}
	}
}