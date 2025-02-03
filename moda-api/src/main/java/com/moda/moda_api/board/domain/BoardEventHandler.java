package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.card.domain.CardInsertForBoardEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BoardEventHandler {
    private final BoardService boardService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCardCreatedForBoard(CardInsertForBoardEvent event) {
        boardService.resetBoardReadStatus(new BoardId(event.getBoardId()));
    }
}
