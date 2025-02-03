package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.domain.BoardId;
import com.moda.moda_api.board.domain.ReadBoardRepository;
import com.moda.moda_api.board.infrastructure.entity.ReadBoardEntity;
import com.moda.moda_api.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("ReadBoardEntity 저장 전 - userId: {}, boardId: {}",
                userId.getValue(), boardId.getValue());
        ReadBoardEntity savedEntity = ReadBoardEntity.builder()
                .userId(userId.getValue())
                .boardId(boardId.getValue())
                .build();
        log.info("ReadBoardEntity 저장 후 - userId: {}, boardId: {}", savedEntity.getUserId(), savedEntity.getBoardId());
        readBoardJpaRepository.save(savedEntity);
    }
}
