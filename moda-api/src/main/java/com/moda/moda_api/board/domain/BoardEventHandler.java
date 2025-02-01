package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.card.domain.CardCreatedForBoardEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardEventHandler {
    private final BoardService boardService;

    @EventListener
    public void handleCardCreatedForBoard(CardCreatedForBoardEvent event) {
        boardService.resetBoardReadStatus(new BoardId(event.getBoardId()));
    }
}
