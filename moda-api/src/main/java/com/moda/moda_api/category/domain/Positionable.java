package com.moda.moda_api.category.domain;

public interface Positionable {
    Position getPosition();
    void movePosition(Position position);
}
