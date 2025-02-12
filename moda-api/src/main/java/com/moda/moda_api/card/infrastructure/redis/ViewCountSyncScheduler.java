package com.moda.moda_api.card.infrastructure.redis;

import com.moda.moda_api.card.infrastructure.repository.CardJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ViewCountSyncScheduler {
    private final RedisTemplate<String, String> redisTemplate;
    private final CardJpaRepository cardJpaRepository;
    private static final String VIEW_COUNT_KEY = "card:viewCount:";

    public ViewCountSyncScheduler(
            CardJpaRepository cardJpaRepository,
            @Qualifier("viewCountRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.cardJpaRepository = cardJpaRepository;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void syncViewCounts() {
        log.info("조회수 동기화를 시작합니다...");

        try (Cursor<String> cursor = redisTemplate.scan(
                ScanOptions.scanOptions()
                        .match(VIEW_COUNT_KEY + "*")
                        .count(100)
                        .build()
        )) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                String cardId = key.substring(VIEW_COUNT_KEY.length());
                String count = redisTemplate.opsForValue().get(key);

                if (count != null) {
                    // DB와 동기화 (업데이트)
                    cardJpaRepository.updateViewCount(cardId, Integer.parseInt(count));

                    // Redis에 존재하는 해당 키 삭제
                    redisTemplate.delete(key);
                }
            }
        }
        log.info("조회수 동기화가 성공적으로 끝났습니다.");
    }
}
