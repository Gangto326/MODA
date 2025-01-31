package com.moda.moda_api.card.infrastructure.repository;

import com.moda.moda_api.card.infrastructure.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<CardEntity, String> {
}
