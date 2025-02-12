package com.moda.moda_api.card.infrastructure.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
@Slf4j
public class HotTopicCleanupScheduler {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String HOT_TOPIC_KEY = "trending:keywords";

    public HotTopicCleanupScheduler(
            @Qualifier("hotTopicRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정마다 호출
    public void cleanupOldData() {
        log.info("7일이 지난 핫토픽 데이터를 제거합니다...");

        LocalDate oldDate = LocalDate.now().minusDays(7);

        // 삭제할 데이터 키 생성
        String oldKey = "trending:keywords:daily:" +
                oldDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // 삭제할 데이터를 전부 가져옴
        Set<ZSetOperations.TypedTuple<String>> oldScores =
                redisTemplate.opsForZSet().rangeWithScores(oldKey, 0, -1);
        
        // 삭제할 값이 존재하면 실행
        if (oldScores != null) {
            for (ZSetOperations.TypedTuple<String> tuple : oldScores) {
                String keyword = tuple.getValue();
                Double score = tuple.getScore();

                // 삭제된 키워드의 점수만큼 현재 키워드에서 차감
                if (keyword != null && score != null) {
                    redisTemplate.opsForZSet().incrementScore(HOT_TOPIC_KEY, keyword, -score);
                }
            }
        }
        
        // 오래된 키워드 삭제
        redisTemplate.delete(oldKey);

        log.info("오래된 키워드가 성공적으로 삭제되었습니다.");
    }
}
