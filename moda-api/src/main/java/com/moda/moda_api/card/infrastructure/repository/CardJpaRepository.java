package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<CardEntity, String> {
    Slice<CardEntity> findByBoardUserIdAndBoardId(String userId, String boardId, Pageable pageable);
}
