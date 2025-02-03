package com.moda.moda_api.board.domain;

import com.moda.moda_api.user.domain.UserId;

import java.util.List;

public interface ReadBoardRepository {
    void deleteByBoardId(BoardId boardId);

    List<BoardId> findReadBoardIds(UserId userId);

    void save(UserId userId, BoardId boardId);
}
