package com.moda.moda_api.card.infrastructure.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HotTopicRankingScheduler {
    private final HotTopicRepositoryImpl hotTopicRepositoryImpl;

    public HotTopicRankingScheduler(HotTopicRepositoryImpl hotTopicRepositoryImpl) {
        this.hotTopicRepositoryImpl = hotTopicRepositoryImpl;
    }

    @Scheduled(cron = "0 0 */3 * * *")  // 30분마다 실행
    public void saveCurrentRanking() {
        log.info("핫 토픽 랭킹을 갱신합니다...");
        hotTopicRepositoryImpl.savePreviousTop();
        log.info("갱신을 완료하였습니다.");
    }
}