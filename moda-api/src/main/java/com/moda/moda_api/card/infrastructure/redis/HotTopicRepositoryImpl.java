package com.moda.moda_api.card.infrastructure.redis;

import com.moda.moda_api.card.application.response.HotTopicResponse;
import com.moda.moda_api.card.domain.HotTopicRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HotTopicRepositoryImpl implements HotTopicRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String HOT_TOPIC_KEY = "trending:keywords";
    private static final String PREVIOUS_TOP_KEY = "trending:keywords:previous_top";

    public HotTopicRepositoryImpl(@Qualifier("hotTopicRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void incrementKeywordScore(String keyword) {
        // 전체 순위에 추가/증가
        redisTemplate.opsForZSet().incrementScore(HOT_TOPIC_KEY, keyword, 1);

        // 일별 순위에 추가/증가
        String dailyKey = "trending:keywords:daily:" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        redisTemplate.opsForZSet().incrementScore(dailyKey, keyword, 1);
    }

    /**
     * 이전 순위의 10개를 저장합니다.
     *
     * 스케쥴러에서 검색어 순위 변경 시간마다 호출합니다.
     */
    @Override
    public void savePreviousTop() {
        Set<String> currentTop = redisTemplate.opsForZSet()
                .reverseRange(HOT_TOPIC_KEY, 0, 9);

        // 기존 리스트 삭제 후 새로 저장
        redisTemplate.delete(PREVIOUS_TOP_KEY);
        redisTemplate.opsForList().rightPushAll(PREVIOUS_TOP_KEY,
                new ArrayList<>(currentTop));
    }

    /**
     * 상위 키워드를 조회합니다.
     * @param limit
     * @return
     */
    @Override
    public List<String> getTopKeywords(Integer limit) {
        Set<String> topKeywords = redisTemplate.opsForZSet()
                .reverseRange(HOT_TOPIC_KEY, 0, limit - 1);
        return new ArrayList<>(topKeywords);
    }

    /**
     * 상위 키워드를 이전의 키워드와 비교하여 메타데이터와 함께 반환합니다.
     * @param limit
     * @return
     */
    @Override
    public List<HotTopicResponse> getTopKeywordsWithChange(Integer limit) {
        // 현재 상위 키워드 limit개 가져오기
        List<String> currentTop = getTopKeywords(limit);

        // 이전에 저장해둔 상위 키워드 리스트 가져오기
        List<String> previousTop = redisTemplate.opsForList().range(PREVIOUS_TOP_KEY, 0, -1);

        // currentTop 리스트를 스트림으로 변환하여 처리
        return currentTop.stream()
                .map(keyword -> {
                    // 이전 순위 찾기 (없으면 -1)
                    int previousRank = previousTop != null ? previousTop.indexOf(keyword) : -1;
                    // 현재 순위
                    int currentRank = currentTop.indexOf(keyword);

                    // 순위 변동 계산
                    int change;
                    if (previousRank == -1) {
                        change = 100;  // 새로 진입한 경우
                    } else if (previousRank == currentRank) {
                        change = 0;    // 순위 유지
                    } else {
                        change = previousRank - currentRank;  // 순위 변동
                    }

                    // HotTopicResponse 객체로 변환
                    return HotTopicResponse.builder()
                            .rank(currentRank + 1)  // 0부터 시작하는 인덱스를 1부터 시작하는 순위로 변환
                            .topic(keyword)
                            .change(change)  // 양수: 순위 상승, 음수: 순위 하락, 0: 새로운 키워드 또는 변동 없음
                            .build();
                })
                .collect(Collectors.toList());
    }
}
