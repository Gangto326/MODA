package com.moda.moda_api.card.application.service;

import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.card.application.mapper.CardDtoMapper;
import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.domain.*;
import com.moda.moda_api.card.exception.CardNotFoundException;
import com.moda.moda_api.common.pagination.SliceRequestDto;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardFactory cardFactory;
    private final CardDtoMapper cardDtoMapper;

    private final BoardService boardService;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * URL을 입력 받고 새로운 카드 생성 후 알맞은 보드로 이동합니다.
     * @param userId
     * @param url
     * @return
     */
    @Transactional
    public Boolean createCard(String userId, String url) {
        UserId userIdObj = new UserId(userId);

        // TODO: (종원) url로 AI API 메서드 호출

        // TODO: (종헌) 임베딩 메서드 호출
        BoardId boardIdObj = new BoardId("");

        Card card = cardFactory.create(userIdObj,
                boardIdObj,
                0,
                null,
                null,
                null,
                null,
                null,
                null);


        Card savedCard = cardRepository.save(card);

        // 새로운 카드가 해당 보드에 INSERT되었다는 이벤트 발생
        eventPublisher.publishEvent(CardCreatedForBoardEvent.from(card));

        return true;
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
    public SliceResponseDto<CardDetailResponse> getCardDetailList(
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

        // 페이지네이션 메타 데이터와 함께 반환합니다.
        return SliceResponseDto.of(
                cards.map(cardDtoMapper::toResponse)
        );
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
        List<CardId> cardIdList = Arrays.stream(cardIds.split(","))
                .map(cardId -> new CardId(cardId))
                .collect(Collectors.toList());

        // 제거할 CardId별 Card 가져오기
        List<Card> cardsToDelete = cardIdList.stream()
                .map(cardId -> cardRepository.findByCardId(cardId.getValue())
                    .orElseThrow(() -> new CardNotFoundException("카드를 찾을 수 없습니다: " + cardId.getValue())))
                .collect(Collectors.toList());

        // 각 Card 객체가 존재하는 BoardId를 가져오기. Set로 중복 제거
        Set<BoardId> boardIds = cardsToDelete.stream()
                .map(Card::getBoardId)
                .collect(Collectors.toSet());

        // 보드에 존재하는 UserId로 삭제 권한 검증 (해당 Card가 User의 카드인지 검증)
        boardService.someOtherBoardOperation(userIdObj, boardIds);
        
        // 카드 삭제
        return cardRepository.deleteAll(cardsToDelete);
    }
}
