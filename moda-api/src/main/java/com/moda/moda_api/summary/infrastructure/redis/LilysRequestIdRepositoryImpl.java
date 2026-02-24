package com.moda.moda_api.summary.infrastructure.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.moda.moda_api.summary.domain.LilysRequestIdRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LilysRequestIdRepositoryImpl implements LilysRequestIdRepository {
	private final RedisTemplate<String, String> urlDuplicatedTemplate;

	private static final String KEY_PREFIX = "lilys_request_id:";
	private static final long TTL_MINUTES = 30;

	@Override
	public void save(String urlHash, String requestId) {
		String key = KEY_PREFIX + urlHash;
		urlDuplicatedTemplate.opsForValue().set(key, requestId, TTL_MINUTES, TimeUnit.MINUTES);
	}

	@Override
	public Optional<String> findByUrlHash(String urlHash) {
		String key = KEY_PREFIX + urlHash;
		return Optional.ofNullable(urlDuplicatedTemplate.opsForValue().get(key));
	}

	@Override
	public void delete(String urlHash) {
		String key = KEY_PREFIX + urlHash;
		urlDuplicatedTemplate.delete(key);
	}
}
