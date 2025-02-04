package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardJpaRepository extends JpaRepository<CardEntity, String> {
    Slice<CardEntity> findByUserIdAndCategoryId(String userId, Long categoryId, Pageable pageable);

    Optional<CardEntity> findByUserIdAndCardId(String userId, String cardId);
}
