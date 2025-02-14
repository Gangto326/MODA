package com.moda.moda_api.user.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.moda.moda_api.user.domain.UserId;
import org.springframework.stereotype.Repository;

import com.moda.moda_api.user.domain.RefreshToken;
import com.moda.moda_api.user.domain.RefreshTokenRepository;
import com.moda.moda_api.user.infrastructure.entity.RefreshTokenEntity;
import com.moda.moda_api.user.infrastructure.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
	private final RefreshTokenJpaRepository refreshTokenJpaRepository;
	private final RefreshTokenMapper refreshTokenMapper;

	@Override
	public RefreshToken save(RefreshToken refreshToken) {
		RefreshTokenEntity entity = refreshTokenMapper.fromDomain(refreshToken);
		return refreshTokenMapper.toDomain(refreshTokenJpaRepository.save(entity));
	}

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenJpaRepository.findByTokenAndIsActiveTrue(token)
			.map(refreshTokenMapper::toDomain);
	}

	@Override
	public void deleteExpiredTokens(LocalDateTime dateTime) {
		refreshTokenJpaRepository.deleteByExpiresAtBefore(dateTime);
	}

	@Override
	public Optional<RefreshToken> findByTokenAndUserNameAndIsActiveTrue(String token, UserId userId) {
		return refreshTokenJpaRepository.findByTokenAndUserIdAndIsActiveTrue(token, userId.getValue())
				.map(refreshTokenMapper::toDomain);
	}

	@Override
	public Optional<RefreshToken> findByTokenAndIsActiveTrue(String token) {
		return refreshTokenJpaRepository.findByTokenAndIsActiveTrue(token)
				.map(refreshTokenMapper::toDomain);
	}
}
