package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.domain.Board;
import com.moda.moda_api.board.domain.BoardRepository;
import com.moda.moda_api.board.infrastructure.mapper.BoardEntityMapper;
import com.moda.moda_api.board.infrastructure.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public boolean delete(Board board) {
        BoardEntity entity = boardEntityMapper.toEntity(board);
        boardJpaRepository.delete(entity);
        return true;
    }

    @Override
    public boolean existsByBoardIdAndUserId(String boardId, String userId) {
        return boardJpaRepository.existsByBoardIdAndUserId(boardId, userId);
    }

    @Override
    public Optional<Board> findByBoardId(String boardId) {
        return boardJpaRepository.findById(boardId)
                .map(boardEntityMapper::toDomain);
    }

    @Override
    public List<Board> findByUserIdOrderByPosition(String userId) {
        return boardJpaRepository.findByUserIdOrderByPosition(userId)
                .stream().map(boardEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Board> boardList) {

        boardJpaRepository.saveAll(boardList.stream()
                .map(boardEntityMapper::toEntity)
                .collect(Collectors.toList()));
    }
}
