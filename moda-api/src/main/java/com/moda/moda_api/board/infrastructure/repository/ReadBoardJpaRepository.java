package com.moda.moda_api.board.infrastructure.repository;

import com.moda.moda_api.board.infrastructure.entity.ReadBoardEntity;
import com.moda.moda_api.board.infrastructure.entity.ReadBoardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReadBoardJpaRepository extends JpaRepository<ReadBoardEntity, ReadBoardId> {

    @Modifying
    @Query("DELETE FROM ReadBoardEntity r WHERE r.boardId = :boardId")
    void deleteByBoardId(String boardId);

    List<ReadBoardEntity> findByUserId(String userId);
}
