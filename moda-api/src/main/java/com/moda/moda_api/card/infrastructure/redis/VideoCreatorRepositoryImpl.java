package com.moda.moda_api.card.infrastructure.redis;

import com.moda.moda_api.card.domain.VideoCreatorRepository;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VideoCreatorRepositoryImpl implements VideoCreatorRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public VideoCreatorRepositoryImpl(@Qualifier("videoRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * user별 저장된 크리에이터 이름을 가져옵니다.
     * @param userId
     * @return
     */
    @Override
    public String getCreatorByUserId(UserId userId) {
        String key = "user:creator:" + userId;
        return redisTemplate.opsForValue().get(key);
    }
}
