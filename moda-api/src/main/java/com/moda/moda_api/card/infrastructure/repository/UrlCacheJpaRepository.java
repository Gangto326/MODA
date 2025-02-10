package com.moda.moda_api.card.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.moda.moda_api.card.infrastructure.entity.UrlCacheEntity;

public interface UrlCacheJpaRepository extends JpaRepository<UrlCacheEntity, String> {
}
