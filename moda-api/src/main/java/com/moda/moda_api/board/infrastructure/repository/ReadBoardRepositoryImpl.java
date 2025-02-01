package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.domain.ReadBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReadBoardRepositoryImpl implements ReadBoardRepository {
    private final ReadBoardJpaRepository readBoardJpaRepository;

    @Override
    public void deleteByBoardId(String boardId) {
        readBoardJpaRepository.deleteByBoardId(boardId);
    }
}
