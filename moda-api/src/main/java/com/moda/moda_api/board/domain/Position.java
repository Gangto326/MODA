package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.exception.InvalidPositionException;
import com.moda.moda_api.board.exception.PositionUpdateException;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
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
     * sourcePosition과 targetPosition의 대소관계를 비교하여
     * 변경 대상인 보드의 position을 +1 또는 -1 처리
     *
     * @param boardList 재조정할 보드 리스트
     */
    public static void adjustPositions(List<Board> boardList, Position source, Position target) {
        if (source.getValue().equals(target.getValue())) {
            throw new PositionUpdateException("같은 위치로는 이동할 수 없습니다.");
        }

        // 범위 내 보드의 움직임을 나타내는 Boolean
        boolean isMovingDown = source.getValue() < target.getValue();

        boardList.forEach(board -> {
            int position = board.getPosition().getValue();

            // source가 target보다 크면 범위 내의 모든 보드를 위로 이동
            if (!isMovingDown && position >= target.getValue() && position < source.getValue()) {
                board.movePosition(board.getPosition().next());
            }
            // source가 target보다 작으면 범위 내의 모든 보드를 아래로 이동
            else if (isMovingDown && position > source.getValue() && position <= target.getValue()) {
                board.movePosition(board.getPosition().before());
            }
        });
    }

    /**
     * Board 리스트를 Position 값 기준으로 정렬
     * @param boardList 정렬할 보드 리스트
     * @return 정렬된 보드 리스트
     */
    public static List<Board> sortByPosition(List<Board> boardList) {
        return boardList.stream()
                .sorted(Comparator.comparing(board -> board.getPosition().getValue()))
                .collect(Collectors.toList());
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
