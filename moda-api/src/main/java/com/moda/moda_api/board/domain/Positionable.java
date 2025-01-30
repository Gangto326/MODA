package com.moda.moda_api.board.domain;

/**
 * Position 조작이 가능한 객체에 상속
 */
public interface Positionable {
    Position getPosition();

    void movePosition(Position position);
}
