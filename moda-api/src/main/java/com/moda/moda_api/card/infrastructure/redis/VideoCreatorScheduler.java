package com.moda.moda_api.card.infrastructure.redis;

import com.moda.moda_api.card.exception.CreatorSaveException;
import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import com.moda.moda_api.card.infrastructure.repository.CardJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class VideoCreatorScheduler {
    private static final String VIDEO_CREATOR_KEY = "user:creator:";
    private final CardJpaRepository cardJpaRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public VideoCreatorScheduler(
            CardJpaRepository cardJpaRepository,
            @Qualifier("videoRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.cardJpaRepository = cardJpaRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 3 * * *")
//    @Scheduled(cron = "0 */1 * * * *")
    public void saveVideoCreators() {
        log.info("영상 제작자 정보 저장을 시작합니다...");

        try {
            // 콘텐츠 타입이 VIDEO인 모든 CardEntity 조회
            List<CardEntity> videoCards = cardJpaRepository.findAllByTypeId(1);

            // userId별 크리에이터 키워드 List로 그룹화
            Map<String, List<String>> userCreators = videoCards.stream()
                    // 키워드가 존재하는지 확인
                    .filter(card -> card.getKeywords() != null && card.getKeywords().length > 0)
                    
                    // 키워드 배열의 0번째에 고정으로 크리에이터 이름이 들어가있음
                    .collect(Collectors.groupingBy(
                            CardEntity::getUserId,
                            Collectors.mapping(
                                    card -> card.getKeywords()[0],
                                    Collectors.toList()
                            )
                    ));

            // 각 유저별로 랜덤 크리에이터 선택 및 저장
            userCreators.forEach((userId, creators) -> {
                if (!creators.isEmpty()) {
                    int randomIndex = (int) (Math.random() * creators.size());
                    // 랜덤 선택 된 index값의 크리에이터를 가져옴
                    String selectedCreator = creators.get(randomIndex);

                    // 레디스에 저장할 Key값 생성
                    String key = VIDEO_CREATOR_KEY + userId;

                    // 해당 Key 값으로 크리에이터 저장
                    redisTemplate.opsForValue().set(key, selectedCreator);
                }
            });

            log.info("영상 제작자 정보 저장이 완료되었습니다.");
        } catch (Exception e) {
            throw new CreatorSaveException("스케쥴러를 사용한 영상 제작자 정보 저장에 실패하였습니다.");
        }
    }
}
