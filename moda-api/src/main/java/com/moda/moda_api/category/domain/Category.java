package com.moda.moda_api.category.domain;

import com.moda.moda_api.category.exception.InvalidPositionException;
import com.moda.moda_api.user.domain.UserId;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category implements Positionable {
    private CategoryId categoryId;
    private UserId userId;
    private Position position;
    private String category;

    @Override
    public void movePosition(Position position) {
        this.position = position;
    }

    public void movePosition(Position position, Position maxPosition) {
        validatePositionRange(position, maxPosition);
        this.position = position;
    }

    private void validatePositionRange(Position position, Position maxPosition) {
        if (position.getValue() > maxPosition.getValue()) {
            throw new InvalidPositionException("Position은 카테고리의 최대 개수를 초과할 수 없습니다.");
        }
    }

    public void validateCurrentPosition(Position sourcePosition) {
        if (!this.position.equals(sourcePosition)) {
            throw new InvalidPositionException("현재 카테고리의 위치가 일치하지 않습니다. 현재 위치: " + this.position.getValue() + ", 요청 위치: " + sourcePosition.getValue());
        }
    }
}
