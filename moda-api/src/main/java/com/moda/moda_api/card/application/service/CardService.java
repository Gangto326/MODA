package com.moda.moda_api.card.application.service;

import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.card.application.mapper.CardDtoMapper;
import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardFactory;
import com.moda.moda_api.card.domain.CardRepository;
import com.moda.moda_api.common.pagination.SliceRequestDto;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardFactory cardFactory;
    private final CardDtoMapper cardDtoMapper;

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

        Card card = cardFactory.create(userIdObj,
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null);

        Card savedCard = cardRepository.save(card);
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


}
