package com.moda.moda_api.card.application.service;

import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.card.application.mapper.CardDtoMapper;
import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.domain.*;
import com.moda.moda_api.card.exception.CardNotFoundException;
import com.moda.moda_api.card.presentation.request.MoveCardRequest;
import com.moda.moda_api.card.presentation.request.UpdateCardRequest;
import com.moda.moda_api.common.pagination.SliceRequestDto;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.summary.application.service.LilysSummaryService;
import com.moda.moda_api.user.domain.UserId;
import com.moda.moda_api.util.hash.HashUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CardService {
    private final CardRepository cardRepository;
    private final CardFactory cardFactory;
    private final CardDtoMapper cardDtoMapper;
    private final BoardService boardService;
    private final ApplicationEventPublisher eventPublisher;
    private final LilysSummaryService lilysSummaryService;
    private final HashUtil hashUtil;

    /**
     * URL을 입력 받고 새로운 카드 생성 후 알맞은 보드로 이동합니다.
     * @param userId
     * @param url
     * @return
     */
    @Transactional
    public CompletableFuture<Boolean> createCard(String userId, String url) {


        UserId userIdObj = new UserId(userId);
        BoardId boardIdObj = new BoardId("18e7e5bc-fd34-41c3-9097-8992925e0048");

        // AI 요약 서비스 호출 후 카드 생성 로직 수행
        return lilysSummaryService.summarize(url)
            .thenCompose(summaryResponse -> {
                // TODO: (종헌) 임베딩 메서드 호출
                // 임시로 랜덤 임베딩 사용
                log.info("Summary Title: {}", summaryResponse.getTitle());
                log.info("Summary Content: {}", summaryResponse.getContent());
                log.info("Thumbnail URL: {}", summaryResponse.getThumbnailUrl());
                log.info("Thumbnail Content: {}", summaryResponse.getThumbnailContent());

                float[] embedding = new float[768];
                for (int i = 0; i < 768; i++) {
                    embedding[i] = (float) (Math.random() * 2 - 1);
                }



                log.info("정보" ,  summaryResponse.getTitle() );
                Card card = cardFactory.create(
                    userIdObj,
                    boardIdObj,
                    1,
                    "asdasdasdasd",
                    summaryResponse.getTitle(),
                    summaryResponse.getContent(),
                    summaryResponse.getThumbnailContent(),
                    summaryResponse.getThumbnailUrl(),
                    new EmbeddingVector(embedding)
                );

                Card savedCard = cardRepository.save(card);

                // 이벤트 발행
                eventPublisher.publishEvent(CardCreatedForBoardEvent.from(savedCard));

                return CompletableFuture.completedFuture(true);
            });
    }

    /**
     * 페이지네이션에 맞게 카드의 리스트 반환합니다.
     *
     * 최대 15개의 카드를 반환하며 Slice 형식으로 다음 값이 있는지를 SliceInfo에 함께 반환
     * @param userId
     * @param boardId
     * @param page
     * @param size
     * @param sortBy
     * @param sortDirection
     * @return
     */
    public SliceResponseDto<CardListResponse> getCardList(
            String userId, String boardId, Integer page, Integer size, String sortBy, String sortDirection
    ) {
        UserId userIdObj = new UserId(userId);
        BoardId boardIdObj = new BoardId(boardId);
        
        // Slice 값 생성
        SliceRequestDto sliceRequestDto = SliceRequestDto.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        // Slice와 파라미터 조건에 맞는 카드를 가져옵니다.
        Slice<Card> cards = cardRepository.findByBoardUserIdAndBoardId(
                userIdObj.getValue(),
                boardIdObj.getValue(),
                sliceRequestDto.toPageable()
        );

        // 보드를 확인했다는 이벤트 발생
        eventPublisher.publishEvent(BoardReadEvent.from(userIdObj, boardIdObj));

        // 페이지네이션 메타 데이터와 함께 반환합니다.
        return SliceResponseDto.of(
                cards.map(cardDtoMapper::toResponse)
        );
    }

    /**
     * 카드의 Detail 정보 반환
     * @param userId
     * @param cardId
     * @return
     */
    public CardDetailResponse getCardDetail(String userId, String cardId) {
        UserId userIdObj = new UserId(userId);
        CardId cardIdObj = new CardId(cardId);
        
        // 카드 탐색
        Card card = findCard(cardIdObj);
        return cardDtoMapper.toDetailResponse(card);
    }

    /**
     * , 로 나뉜 String을 받고 입력으로 받은 모든 카드를 제거합니다.
     * @param userId
     * @param cardIds
     * @return
     */
    @Transactional
    public Boolean deleteCard(String userId, String cardIds) {
        UserId userIdObj = new UserId(userId);

        // ,로 구분된 cardId들을 Value Object로 매핑하며 id 검증
        List<CardId> cardIdList = toCardIds(Arrays.asList(cardIds.split(",")));

        // 제거할 CardId별 Card 가져오기
        List<Card> cardsToDelete = findCardList(cardIdList);

        // Card 삭제 권한 검증
        validateOwnership(userIdObj, cardsToDelete);
        
        // 카드 삭제
        return cardRepository.deleteAll(cardsToDelete);
    }

    /**
     * 카드의 Content를 변경합니다.
     * @param userId
     * @param request
     * @return
     */
    @Transactional
    public CardDetailResponse updateCardContent(String userId, UpdateCardRequest request) {
        UserId userIdObj = new UserId(userId);
        CardId cardIdObj = new CardId(request.getCardId());
        
        // Content를 변경할 카드 탐색
        Card card = findCard(cardIdObj);

        // Content 변경 권한 검증
        validateOwnership(userIdObj, List.of(card));
        
        // Card의 콘텐츠 변경 후 저장
        card.changeContent(request.getContent());
        cardRepository.save(card);

        return cardDtoMapper.toDetailResponse(card);
    }

    /**
     * 입력으로 들어온 모든 카드를 BordId에 해당하는 보드로 옮깁니다.
     * @param userId
     * @param request
     * @return
     */
    @Transactional
    public Boolean updateCardBoard(String userId, MoveCardRequest request) {
        UserId userIdObj = new UserId(userId);
        BoardId boardIdObj = new BoardId(request.getBoardId());
        
        // 입력의 모든 CardId를 Value Object로 매핑
        List<CardId> cardIdList = toCardIds(request.getCardIdList());
        
        // CardId에 해당하는 모든 카드를 가져옴
        List<Card> cardsToMove = findCardList(cardIdList);

        // 각 카드의 변경 권한 검증
        validateOwnership(userIdObj, cardsToMove);

        // 모든 카드를 입력으로 들어온 BoardId로 이동 후 저장
        cardsToMove.stream().forEach(card -> card.moveBoard(boardIdObj));
        cardRepository.saveAll(cardsToMove);

        return true;
    }

    /**
     * String으로 들어오는 모든 CardId 입력을 CardId Value Object로 변환하여 반환합니다.
     * @param cardIds
     * @return
     */
    private List<CardId> toCardIds(List<String> cardIds) {
        return cardIds.stream()
                .map(CardId::new)
                .collect(Collectors.toList());
    }

    /**
     * CardId로 Card를 찾습니다.
     * @param cardId
     * @return
     */
    private Card findCard(CardId cardId) {
        return cardRepository.findByCardId(cardId.getValue())
                .orElseThrow(() -> new CardNotFoundException("카드를 찾을 수 없습니다."));
    }

    /**
     * CardIdList에 존재하는 모든 Card를 찾습니다.
     * @param cardIdList
     * @return
     */
    private List<Card> findCardList(List<CardId> cardIdList) {
        return cardIdList.stream()
                .map(this::findCard)
                .collect(Collectors.toList());
    }

    /**
     * Card별 User의 소유권 권한 검증 로직
     * @param userId
     * @param cardList
     */
    private void validateOwnership(UserId userId, List<Card> cardList) {
        // 각 Card 객체가 존재하는 BoardId를 가져오기. Set로 중복 제거
        Set<BoardId> boardIds = cardList.stream()
                .map(Card::getBoardId)
                .collect(Collectors.toSet());

        // 보드에 존재하는 UserId로 권한 검증 (해당 Card가 User의 카드인지 검증)
        boardService.someOtherBoardOperation(userId, boardIds);
    }
}
