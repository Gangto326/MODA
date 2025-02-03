package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.board.domain.ReadBoardRepository;
import com.moda.moda_api.board.infrastructure.entity.ReadBoardEntity;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReadBoardRepositoryImpl implements ReadBoardRepository {
    private final ReadBoardJpaRepository readBoardJpaRepository;

    @Override
    public void deleteByBoardId(BoardId boardId) {
        readBoardJpaRepository.deleteByBoardId(boardId.getValue());
    }

    @Override
    public List<BoardId> findReadBoardIds(UserId userId) {
        return readBoardJpaRepository.findByUserId(userId.getValue())
                .stream()
                .map(entity -> new BoardId(entity.getBoardId()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(UserId userId, BoardId boardId) {
        ReadBoardEntity entity = ReadBoardEntity.builder()
                .userId(userId.getValue())
                .boardId(boardId.getValue())
                .build();

        readBoardJpaRepository.save(entity);
    }
}
