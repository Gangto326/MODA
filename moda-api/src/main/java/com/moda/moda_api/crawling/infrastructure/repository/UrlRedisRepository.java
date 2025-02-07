package com.moda.moda_api.crawling.infrastructure.repository;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.moda.moda_api.crawling.domain.model.Url;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UrlRedisRepository {
	private final RedisTemplate<String, List<Url>> redisTemplate;

	public void saveUrls(String keyword, List<Url> urls) {
		redisTemplate.opsForValue().set(keyword, urls);
	}

	public List<Url> getUrls(String keyword) {
		return redisTemplate.opsForValue().get(keyword);
	}
}