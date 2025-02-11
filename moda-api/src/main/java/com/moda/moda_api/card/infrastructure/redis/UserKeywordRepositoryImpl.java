package com.moda.moda_api.card.infrastructure.redis;

import com.moda.moda_api.card.domain.UserKeywordRepository;
import com.moda.moda_api.user.domain.UserId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class UserKeywordRepositoryImpl implements UserKeywordRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public UserKeywordRepositoryImpl(@Qualifier("keywordRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveKeywords(UserId userId, String[] keywords) {
        String countKey = "user:keywords:count:" + userId.getValue();
        String dateKey = "user:keywords:date:" + userId.getValue();
        double currentTime = System.currentTimeMillis();

        for (String keyword : keywords) {
            // 해당 키워드의 현재 횟수를 가져옴. 존재하지 않는 키워드면 null
            Double existingScore = redisTemplate.opsForZSet().score(countKey, keyword);
            
            // existingScore이 null인 경우 0으로 초기화
            double count = existingScore != null ? existingScore : 0;
            // 시간에 따른 차등 점수 부여 (같은 점수인 경우 키워드 저장 시간에 따라 아주 미세한 가중치 적용)
            double score = (count + 1) + (currentTime / 1000000000000000.0);

            // incrementScore를 사용하여 이미 존재하는 키워드의 경우 count 증가
            redisTemplate.opsForZSet().incrementScore(countKey, keyword, score);
            
            // 해당 키워드를 저장한 시간을 갱신 (저장한 시점부터 7일 뒤 삭제하기 위함)
            redisTemplate.opsForZSet().add(dateKey, keyword, currentTime);
        }
    }

    @Override
    public List<String> getTopKeywords(UserId userId, int limit) {
        String key = "user:keywords:count:" + userId.getValue();

        // reverseRange로 높은 score로 정렬
        Set<String> keywords = redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1);
        
        // keywords가 존재하지 않으면 빈 배열 반환
        return new ArrayList<>(keywords != null ? keywords : Collections.emptySet());
    }
}
