package com.moda.moda_api.user.domain;

import org.apache.el.stream.Stream;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {
	RefreshToken save(RefreshToken refreshToken);

	Optional<RefreshToken> findByToken(String token);

	void deleteExpiredTokens(LocalDateTime dateTime);

	Optional<RefreshToken> findByTokenAndIsActiveTrue(String token);

	List<RefreshToken> findAllByUserNameAndIsActiveTrue(UserId userId);
}
