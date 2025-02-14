package com.moda.moda_api.common.infrastructure;

import com.moda.moda_api.common.jwt.TokenDto;
import com.moda.moda_api.user.domain.RefreshToken;
import com.moda.moda_api.user.domain.RefreshTokenRepository;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final String ACCESS_TOKEN_PREFIX = "access_token:";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60; // 30분

    public TokenService(
            @Qualifier("jwtRedisTemplate") RedisTemplate<String, String> redisTemplate,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveAccessToken(UserId userId, String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + userId.getValue();
        redisTemplate.opsForValue().set(
                key,
                accessToken,
                ACCESS_TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
        );
    }

    public void saveRefreshToken(UserId userId, TokenDto refreshTokenDto) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenDto.getTokenValue())
                .expiresAt(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(refreshTokenDto.getExpiresAt()),
                        ZoneId.systemDefault()
                ))
                .userId(userId)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    public void invalidateAccessToken(UserId userId) {
        String key = ACCESS_TOKEN_PREFIX + userId.getValue();
        redisTemplate.delete(key);
    }

    // 토큰
    public void invalidateRefreshToken(UserId userId, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndUserNameAndIsActiveTrue(token, userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
        refreshToken.deactivate();
        refreshTokenRepository.save(refreshToken);
    }

    public boolean validateAccessToken(UserId userId, String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + userId.getValue();
        String storedToken = redisTemplate.opsForValue().get(key);
        return accessToken.equals(storedToken);
    }

    public RefreshToken validateRefreshToken(String token) {
        return refreshTokenRepository.findByTokenAndIsActiveTrue(token)
                .filter(rt -> rt.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
    }
}