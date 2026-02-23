package com.moda.moda_api.card.infrastructure.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.moda.moda_api.card.domain.UrlDuplicatedRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UrlDuplicatedRepositoryImpl implements UrlDuplicatedRepository {
	private final RedisTemplate<String, String> urlDuplicatedTemplate;

	private static final String URL_KEY = "duplicated_url:";

	public void urlDuplicatedSave(String url){
		String key = URL_KEY + url;
		urlDuplicatedTemplate.opsForValue().set(key,url,5, TimeUnit.MINUTES);
	}

	public Boolean checkDuplicated(String url){
		String key = URL_KEY + url;
		String savedUrl = urlDuplicatedTemplate.opsForValue().get(key);
		return url.equals(savedUrl);
	}

	public void urlDuplicatedDelete(String url) {
		String key = URL_KEY + url;
		urlDuplicatedTemplate.delete(key);
	}
}
