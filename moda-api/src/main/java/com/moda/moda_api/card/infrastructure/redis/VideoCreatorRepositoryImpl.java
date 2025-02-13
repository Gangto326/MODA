package com.moda.moda_api.card.infrastructure.redis;

import com.moda.moda_api.card.domain.VideoCreatorRepository;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VideoCreatorRepositoryImpl implements VideoCreatorRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String VIDEO_CREATOR_KEY = "user:creator:";

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
        String key = VIDEO_CREATOR_KEY + userId.getValue();
        String value = redisTemplate.opsForValue().get(key);
        return value == null? "": value;
    }
}
