package com.moda.moda_api.card.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.moda.moda_api.card.domain.UrlCache;
import com.moda.moda_api.card.domain.UrlCacheRepository;
import com.moda.moda_api.card.infrastructure.entity.UrlCacheEntity;
import com.moda.moda_api.card.infrastructure.mapper.UrlCacheEntityMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UrlCacheRepositoryImpl implements UrlCacheRepository {
	private final UrlCacheJpaRepository urlCacheJpaRepository;
	private final UrlCacheEntityMapper mapper;

	@Override
	public UrlCache save(UrlCache urlCache) {
		System.out.println("들어오긴 혀?");
		try {
			log.debug("Saving UrlCache: {}", urlCache);
			UrlCacheEntity entity = mapper.toEntity(urlCache);
			log.debug("Converted to entity: {}", entity);
			UrlCacheEntity savedEntity = urlCacheJpaRepository.save(entity);
			log.debug("Saved entity: {}", savedEntity);
			return mapper.toDomain(savedEntity);
		} catch (Exception e) {
			log.error("Error saving UrlCache", e);
			throw e;
		}
	}

	@Override
	public Optional<UrlCache> findByUrlHash(String urlHash) { // 추가
		return urlCacheJpaRepository.findById(urlHash)
			.map(mapper::toDomain);
	}
}