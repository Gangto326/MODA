package com.moda.moda_api.user.domain;

import org.apache.el.stream.Stream;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository {
	RefreshToken save(RefreshToken refreshToken);

	Optional<RefreshToken> findByToken(String token);

	void deleteExpiredTokens(LocalDateTime dateTime);

	Optional<RefreshToken> findByTokenAndUserNameAndIsActiveTrue(String token, UserId userId);

	Optional<RefreshToken> findByTokenAndIsActiveTrue(String token);
}
