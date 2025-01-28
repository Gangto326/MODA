package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.exception.InvalidPositionException;
import lombok.Getter;

@Getter
public class Position {
    private final Integer value;

    public Position(int value) {
        validatePosition(value);
        this.value = value;
    }

    /**
     * position 값이 null인 경우 == 보드가 하나도 없는 경우
     * 보드 index의 시작은 0
     */
    public static Position first() {
        return new Position(0);
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
    private void validatePosition(int value) {
        if (value < 0) {
            throw new InvalidPositionException("보드의 위치는 0이하일 수 없습니다.");
        }
    }
}
