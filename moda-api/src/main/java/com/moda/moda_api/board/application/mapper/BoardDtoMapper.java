package com.moda.moda_api.board.application.mapper;

import com.moda.moda_api.board.application.response.BoardListResponse;
import com.moda.moda_api.board.application.response.BoardResponse;
import com.moda.moda_api.board.domain.Board;
import com.moda.moda_api.board.domain.BoardWithCards;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardDtoMapper {

    // Domain객체를 Response객체로 매핑
    public BoardResponse toResponse(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId().getValue())
                .userId(board.getUserId().getValue())
                .title(board.getTitle())
                .isPublic(board.isPublic())
                .position(board.getPosition().getValue())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public BoardListResponse toBoardListResponse(BoardWithCards boardWithCards) {
        return BoardListResponse.builder()
                .board(toResponse(boardWithCards.getBoard()))
                .recentCards(boardWithCards.getRecentCards().stream()
                        .map(this::toCardListResponse)
                        .collect(Collectors.toList()))
                .isRead(boardWithCards.getIsRead())
                .build();
    }

    private CardListResponse toCardListResponse(Card card) {
        return CardListResponse.builder()
                .cardId(card.getCardId().getValue())
                .boardId(card.getBoardId().getValue())
                .typeId(card.getTypeId())
                .type(ContentType.from(card.getTypeId()))
                .thumbnailContent(card.getThumbnailContent())
                .thumbnailUrl(card.getThumbnailUrl())
                .createdAt(card.getCreatedAt())
                .build();
    }
}
