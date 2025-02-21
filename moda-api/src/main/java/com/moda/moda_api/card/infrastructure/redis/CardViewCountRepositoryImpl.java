package com.moda.moda_api.card.infrastructure.redis;

import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.card.domain.CardViewCountRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CardViewCountRepositoryImpl implements CardViewCountRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String VIEW_COUNT_KEY = "card:viewCount:";

    public CardViewCountRepositoryImpl(@Qualifier("viewCountRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void incrementViewCount(CardId cardId) {
        String key = VIEW_COUNT_KEY + cardId.getValue();
        redisTemplate.opsForValue().increment(key);
    }
}
