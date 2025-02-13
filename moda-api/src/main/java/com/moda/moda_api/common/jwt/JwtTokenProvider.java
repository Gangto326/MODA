package com.moda.moda_api.common.jwt;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.moda.moda_api.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
	// private final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 15;  // 15분
	// private final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 6;  // 6시간
	//
	// @Value("${jwt.secret}")
	// private String secretKey;
	//
	// private SecretKey getSecretKey(TokenType tokenType) {
	// 	String keyValue = (tokenType == TokenType.ACCESS) ? secretKey + "_access" : secretKey + "_refresh";
	// 	return Keys.hmacShaKeyFor(Base64.getDecoder().decode(keyValue));
	// }
	//
	// public TokenDto generateToken(User user, TokenType tokenType) {
	// 	Long issuedAt = System.currentTimeMillis();
	// 	Long expiresAt = getExpiration(issuedAt, tokenType);
	//
	// 	String token = Jwts.builder()
	// 		.subject(String.valueOf(user.getUserId())) // userId를 subject로 사용 (숫자라면 String 변환)
	// 		.issuedAt(new Date(issuedAt))
	// 		.expiration(new Date(expiresAt))
	// 		.signWith(getSecretKey(tokenType))
	// 		.compact();
	//
	// 	return TokenDto.builder()
	// 		.userId(user.getUserId())
	// 		.tokenType(tokenType)
	// 		.token(token)
	// 		.issuedAt(issuedAt)
	// 		.expiresAt(expiresAt)
	// 		.build();
	// }
	//
	// private long getExpiration(long issuedAt, TokenType tokenType) {
	// 	return issuedAt + switch (tokenType) {
	// 		case ACCESS -> ACCESS_TOKEN_EXPIRE_TIME;
	// 		case REFRESH -> REFRESH_TOKEN_EXPIRE_TIME;
	// 	};
	// }
	//
	// public boolean validateToken(String token, TokenType tokenType) {
	// 	try {
	// 		Jwts.parser()
	// 			.verifyWith(getSecretKey(tokenType))
	// 			.build()
	// 			.parseSignedClaims(token);
	// 		return true;
	// 	} catch (Exception e) {
	// 		log.warn("JWT validation failed: {}", e.getMessage());  // 로그 추가
	// 		return false;
	// 	}
	// }
	// public String getUserIdFromToken(String token, TokenType tokenType) {
	// 	try {
	// 		Claims claims = Jwts.parser()
	// 			.verifyWith(getSecretKey(tokenType))
	// 			.build()
	// 			.parseSignedClaims(token)
	// 			.getPayload();
	// 		return claims.getSubject();  // subject에서 userId 추출
	// 	} catch (Exception e) {
	// 		log.warn("Failed to extract userId from token: {}", e.getMessage());
	// 		return null;
	// 	}
	// }

}