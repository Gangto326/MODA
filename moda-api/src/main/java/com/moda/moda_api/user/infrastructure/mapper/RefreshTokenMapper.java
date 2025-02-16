package com.moda.moda_api.user.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.moda.moda_api.user.domain.RefreshToken;
import com.moda.moda_api.user.infrastructure.entity.RefreshTokenEntity;

@Component
public class RefreshTokenMapper {
	public RefreshToken toDomain(RefreshTokenEntity entity) {
		return RefreshToken.builder()
			.refreshTokenId(entity.getRefreshTokenId())
			.userId(entity.getUserId())
			.token(entity.getToken())
			.expiresAt(entity.getExpiresAt())
			.createdAt(entity.getCreatedAt())
			.isActive(entity.isActive())
			.build();
	}

	public RefreshTokenEntity fromDomain(RefreshToken refreshToken) {
		return RefreshTokenEntity.builder()
			.refreshTokenId(refreshToken.getRefreshTokenId())
			.token(refreshToken.getToken())
			.userId(refreshToken.getUserId())
			.expiresAt(refreshToken.getExpiresAt())
			.createdAt(refreshToken.getCreatedAt())
			.isActive(refreshToken.isActive())
			.build();
	}
}