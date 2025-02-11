package com.moda.moda_api.card.infrastructure.redis;

import com.moda.moda_api.card.exception.KeywordCleanupException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class KeywordCleanupScheduler {
    private final RedisTemplate<String, String> redisTemplate;

    public KeywordCleanupScheduler(@Qualifier("keywordRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupOldKeywords() {
        log.info("키워드 삭제를 시작합니다...");
        double weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);

        try (Cursor<String> cursor = redisTemplate.scan(
                ScanOptions.scanOptions()
                        .match("user:keywords:date:*")
                        .count(100)
                        .build()
        )) {
            while (cursor.hasNext()) {
                // 커서를 하나씩 이동하며 찾기
                String dateKey = cursor.next();

                // userId 값 파싱
                String userId = dateKey.substring("user:keywords:date:".length());

                // date와 함께 삭제 될 count 값 탐색
                String countKey = "user:keywords:count:" + userId;

                // user:keywords:date:userId 의 score 중 7일 전까지의 키워드(value)를 전부 가져옴
                Set<String> oldKeywords = redisTemplate.opsForZSet().rangeByScore(
                        dateKey, 0, weekAgo
                );

                // 날짜 기록 데이터와 키워드 카운팅 데이터를 전부 삭제
                if (oldKeywords != null && !oldKeywords.isEmpty()) {
                    redisTemplate.opsForZSet().remove(dateKey, oldKeywords.toArray());
                    redisTemplate.opsForZSet().remove(countKey, oldKeywords.toArray());
                }
            }
            log.info("오늘의 키워드 삭제가 성공적으로 끝났습니다.");
        } catch (Exception e) {
            throw new KeywordCleanupException("스케쥴러를 사용한 키워드 정리에 실패하였습니다.");
        }
    }
}