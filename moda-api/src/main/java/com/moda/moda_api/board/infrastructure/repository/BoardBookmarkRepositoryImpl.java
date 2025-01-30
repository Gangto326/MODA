package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.domain.BoardBookmark;
import com.moda.moda_api.board.domain.BoardBookmarkRepository;
import com.moda.moda_api.board.infrastructure.entity.BoardBookmarkEntity;
import com.moda.moda_api.board.infrastructure.mapper.BoardBookmarkEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BoardBookmarkRepositoryImpl implements BoardBookmarkRepository {
    private final BoardBookmarkJpaRepository boardBookmarkJpaRepository;
    private final BoardBookmarkEntityMapper boardBookmarkEntityMapper;

    @Override
    public boolean existsByUserIdAndBoardId(String userId, String boardId) {
        return boardBookmarkJpaRepository.existsByUserIdAndBoardId(userId, boardId);
    }

    @Override
    public void deleteByUserIdAndBoardId(String userId, String boardId) {
        boardBookmarkJpaRepository.deleteByUserIdAndBoardId(userId, boardId);
    }

    @Override
    public Optional<Integer> findLastPosition(String userId) {
        return boardBookmarkJpaRepository.findTopByUserIdOrderByPositionDesc(userId);
    }

    @Override
    public BoardBookmark save(BoardBookmark boardBookmark) {
        BoardBookmarkEntity entity = boardBookmarkEntityMapper.toEntity(boardBookmark);
        BoardBookmarkEntity savedEntity = boardBookmarkJpaRepository.save(entity);
        return boardBookmarkEntityMapper.toDomain(savedEntity);
    }

    @Override
    public List<BoardBookmark> findByUserIdOrderByPosition(String userId) {
        return boardBookmarkJpaRepository.findByUserIdOrderByPositionAsc(userId).stream()
                .map(boardBookmarkEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
