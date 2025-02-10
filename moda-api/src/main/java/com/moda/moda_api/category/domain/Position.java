package com.moda.moda_api.category.domain;

import com.moda.moda_api.category.exception.InvalidPositionException;
import lombok.Value;

@Value
public class Position {
    public static final int MIN_POSITION = 0;
    public static final int MAX_POSITION = 12;
    private final Integer value;

    public Position(int value) {
        validatePosition(value);
        this.value = value;
    }

    /**
     * 최소 Position 값을 가진 Position 객체 반환
     */
    public static Position first() {
        return new Position(MIN_POSITION);
    }

    /**
     * 최대 Position 값을 가진 Position 객체 반환
     */
    public static Position max() {
        return new Position(MAX_POSITION);
    }

    /**
     * 해당 위치를 가지는 position 객체 생성
     */
    public static Position of(int value) {
        return new Position(value);
    }

    /**
     * 다음 위치로 이동
     */
    public Position next() {
        return new Position(this.value + 1);
    }

    /**
     * 이전 위치로 이동
     */
    public Position before() {
        return new Position(this.value - 1);
    }

    /**
     * position 값 검증
     */
    private void validatePosition(Integer value) {
        if (value == null) {
            throw new InvalidPositionException("카테고리의 위치는 null일 수 없습니다.");
        }
        if (value < 0) {
            throw new InvalidPositionException("카테고리의 위치는 0이하일 수 없습니다.");
        }
        if (value > 12) {
            throw new InvalidPositionException("카테고리의 위치는 12이상일 수 없습니다.");
        }
    }
}
