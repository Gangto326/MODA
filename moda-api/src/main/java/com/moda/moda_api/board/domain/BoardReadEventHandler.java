package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.card.domain.BoardReadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BoardReadEventHandler {
    private final BoardService boardService;

    @TransactionalEventListener
    public void handleBoardReadEvent(BoardReadEvent event) {
        boardService.markBoardReadStatus(event.getUserId(), event.getBoardId());
    }
}
