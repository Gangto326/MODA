package com.moda.moda_api.notification.application;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMTokenService {
	private final RedisTemplate<String, String> redisTemplate;
	private static final String KEY_PREFIX = "fcm:token:";
	private static final String USER_PREFIX = "fcm:user:";

	// 토큰 저장
	public void saveToken(String userId, String token) {
		String tokenKey = KEY_PREFIX + token;
		String userKey = USER_PREFIX + userId;

		redisTemplate.opsForValue().set(tokenKey, userId);
		redisTemplate.opsForSet().add(userKey, token);

		redisTemplate.expire(tokenKey, 30, TimeUnit.DAYS);
		redisTemplate.expire(userKey, 30, TimeUnit.DAYS);
	}

	// 사용자의 모든 토큰 조회
	public Set<String> getUserTokens(String userId) {
		String userKey = USER_PREFIX + userId;
		return redisTemplate.opsForSet().members(userKey);
	}

	// 토큰 삭제
	public void removeToken(String token) {
		String tokenKey = KEY_PREFIX + token;
		String userId = redisTemplate.opsForValue().get(tokenKey);

		if (userId != null) {
			String userKey = USER_PREFIX + userId;
			redisTemplate.opsForSet().remove(userKey, token);
			redisTemplate.delete(tokenKey);
		}
	}

	// 토큰 존재 여부 확인
	public boolean existsToken(String token) {
		return redisTemplate.hasKey(KEY_PREFIX + token);
	}
}
