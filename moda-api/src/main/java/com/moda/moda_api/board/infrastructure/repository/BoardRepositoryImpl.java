package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.domain.Board;
import com.moda.moda_api.board.domain.BoardRepository;
import com.moda.moda_api.board.infrastructure.mapper.BoardEntityMapper;
import com.moda.moda_api.board.infrastructure.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {
    private final BoardJpaRepository boardJpaRepository;
    private final BoardEntityMapper boardEntityMapper;

    @Override
    public Board save(Board board) {
        BoardEntity entity = boardEntityMapper.toEntity(board);
        BoardEntity savedEntity = boardJpaRepository.save(entity);
        return boardEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Integer> findLastPosition(String userId) {
        return boardJpaRepository.findTopByOrderByPositionDesc();
    }
}
