package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.application.service.BoardService;
import com.moda.moda_api.card.domain.BoardReadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardReadEventHandler {
    private final BoardService boardService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBoardReadEvent(BoardReadEvent event) {
        log.info("BoardReadEvent 실행 - userId: {}, boardId: {}",
                event.getUserId().getValue(), event.getBoardId().getValue());
        boardService.markBoardReadStatus(event.getUserId(), event.getBoardId());
    }
}
