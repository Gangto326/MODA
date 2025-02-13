package com.moda.moda_api.user.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moda.moda_api.user.infrastructure.entity.RefreshTokenEntity;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
	Optional<RefreshTokenEntity> findByTokenAndIsActiveTrue(String token);
	void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
