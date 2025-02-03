package com.moda.moda_api.board.infrastructure.entity;

import com.moda.moda_api.card.infrastructure.entity.CardEntity;

import java.util.List;

public interface BoardWithCardsProjection {
    BoardEntity getBoard();
    List<CardEntity> getCards();
}
