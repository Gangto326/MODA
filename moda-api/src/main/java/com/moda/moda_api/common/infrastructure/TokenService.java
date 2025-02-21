package com.moda.moda_api.common.infrastructure;

import com.moda.moda_api.card.exception.UnauthorizedException;
import com.moda.moda_api.common.jwt.TokenDto;
import com.moda.moda_api.common.util.JwtUtil;
import com.moda.moda_api.user.domain.RefreshToken;
import com.moda.moda_api.user.domain.RefreshTokenRepository;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;

import org.aspectj.weaver.tools.Trace;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {


    private final RedisTemplate<String, String> jwtRedisTemplate;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    private static final String ACCESS_TOKEN_PREFIX = "access_token:";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60; // 30분 * 60

	//AccessToken저장하기
    public void saveAccessToken(UserId userId, String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + userId.getValue();
		jwtRedisTemplate.opsForValue().set(
                key,
                accessToken,
                ACCESS_TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
        );
    }

	//refreshToken 저장하기
    public void saveRefreshToken(UserId userId, TokenDto refreshTokenDto) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenDto.getTokenValue())
                .expiresAt(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(refreshTokenDto.getExpiresAt()),
                        ZoneId.systemDefault()
                ))
                .userId(userId.getValue())
                .isActive(true)
                .build();

        refreshTokenRepository.save(refreshToken);
    }


	// AccessToken 제거하기
    public void invalidateAccessToken(String accessToken) {
        // accessToken에서 userId 추출
        String userId = jwtUtil.getUserId(accessToken, "AccessToken");
        String key = ACCESS_TOKEN_PREFIX + userId;

        // 해당 키의 토큰 값 확인
        String storedToken = jwtRedisTemplate.opsForValue().get(key);

        if (storedToken != null && storedToken.equals(accessToken)) {
            jwtRedisTemplate.delete(key);
        }
    }


    // refreshToken 제거하기
    public void invalidateRefreshToken(String refreshToken) {
        // refreshToken에서 userId 추출
        String userId = jwtUtil.getUserId(refreshToken, "RefreshToken");

        // 해당 유저의 active한 refresh token들을 조회
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUserIdAndIsActiveTrue(userId);

        // 주어진 refreshToken과 일치하는 것만 비활성화
        for (RefreshToken token : refreshTokens) {
            if (token.getToken().equals(refreshToken)) {
                token.deactivate();
                refreshTokenRepository.save(token);
                break;  // 일치하는 토큰을 찾았으므로 반복 중단
            }
        }
    }

	// AccessToken 살아 있는지 체크하기
    public boolean validateAccessToken(UserId userId, String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + userId.getValue();
        String storedToken = jwtRedisTemplate.opsForValue().get(key);
        return accessToken.equals(storedToken);
    }

	// RefreshToken 살아 있는지 체크하기
    public RefreshToken validateRefreshToken(String token) {
        return refreshTokenRepository.findByTokenAndIsActiveTrue(token)
            .filter(rt -> rt.getExpiresAt().isAfter(LocalDateTime.now()))
            .orElseThrow(() -> new UnauthorizedException("Refresh Token이 만료되었거나 유효하지 않습니다.", "REFRESH_TOKEN_EXPIRED"));
    }

}