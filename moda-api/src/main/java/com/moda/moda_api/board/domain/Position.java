package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.exception.InvalidPositionException;
import com.moda.moda_api.board.exception.PositionUpdateException;
import lombok.Getter;
import lombok.Value;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class Position {
    public static final int MIN_POSITION = 0;
    public static final int MAX_POSITION = Integer.MAX_VALUE;
    private final Integer value;

    public Position(int value) {
        validatePosition(value);
        this.value = value;
    }

    /**
     * 최소 Position 값을 가진 Position 객체 반환
     * position 값이 null인 경우 == 보드가 하나도 없는 경우
     * 보드 index의 시작은 0
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
            throw new InvalidPositionException("보드의 위치는 null일 수 없습니다.");
        }
        if (value < 0) {
            throw new InvalidPositionException("보드의 위치는 0이하일 수 없습니다.");
        }
    }
}
