package com.moda.moda_api.board.application.service;

import com.moda.moda_api.board.domain.Board;
import com.moda.moda_api.board.domain.BoardRepository;
import com.moda.moda_api.board.domain.UserId;
import com.moda.moda_api.board.exception.BoardNotFoundException;
import com.moda.moda_api.board.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthorizationService {
    private final BoardRepository boardRepository;

    public void isBoardOwner(UserId userId, String boardId) {
        if (!boardRepository.existsByBoardIdAndUserId(userId.getValue(), boardId)) {
            throw new UnauthorizedException("권한이 존재하지 않습니다.");
        }
    }


}
