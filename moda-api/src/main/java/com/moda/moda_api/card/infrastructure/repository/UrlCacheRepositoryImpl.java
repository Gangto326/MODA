package com.moda.moda_api.card.infrastructure.repository;


import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.moda.moda_api.card.domain.UrlCache;
import com.moda.moda_api.card.domain.UrlCacheRepository;
import com.moda.moda_api.card.infrastructure.entity.UrlCacheEntity;
import com.moda.moda_api.card.infrastructure.mapper.UrlCacheEntityMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UrlCacheRepositoryImpl implements UrlCacheRepository {
	private final UrlCacheJpaRepository urlCacheJpaRepository;
	private final UrlCacheEntityMapper mapper;

	@Override
	public UrlCache save(UrlCache urlCache) {
		UrlCacheEntity entity = mapper.toEntity(urlCache);
		return mapper.toDomain(urlCacheJpaRepository.save(entity));
	}

	@Override
	public Optional<UrlCache> findByUrlHash(String urlHash) { // 추가
		return urlCacheJpaRepository.findById(urlHash)
			.map(mapper::toDomain);
	}
}