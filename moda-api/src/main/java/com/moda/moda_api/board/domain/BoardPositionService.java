package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.exception.PositionUpdateException;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BoardPositionService {
    /**
     * sourcePosition과 targetPosition의 대소관계를 비교하여
     * 변경 대상인 보드의 position을 +1 또는 -1 처리
     *
     * @param items 재조정할 보드 리스트
     */
    public void adjustPositions(List<? extends Positionable> items, Position source, Position target) {
        if (source.getValue().equals(target.getValue())) {
            throw new PositionUpdateException("같은 위치로는 이동할 수 없습니다.");
        }

        // 범위 내 보드의 움직임을 나타내는 Boolean
        boolean isMovingDown = source.getValue() < target.getValue();

        items.forEach(item -> {
            int position = item.getPosition().getValue();

            // source가 target보다 크면 범위 내의 모든 보드를 위로 이동
            if (!isMovingDown && position >= target.getValue() && position < source.getValue()) {
                item.movePosition(item.getPosition().next());
            }
            // source가 target보다 작으면 범위 내의 모든 보드를 아래로 이동
            else if (isMovingDown && position > source.getValue() && position <= target.getValue()) {
                item.movePosition(item.getPosition().before());
            }
        });
    }

    /**
     * Board 리스트를 Position 값 기준으로 정렬
     * @param items 정렬할 보드 리스트
     * @return 정렬된 보드 리스트
     */
    public <T extends Positionable> List<T> sortByPosition(List<T> items) {
        return items.stream()
                .sorted(Comparator.comparing(item -> item.getPosition().getValue()))
                .collect(Collectors.toList());
    }

    /**
     * lastPosition이 1000 이상이면 reorderAndGetNextPosition를 호출하여 재정렬
     * @param lastPosition
     * @param items
     * @return
     */
    public Position getNextPosition(Optional<Integer> lastPosition, List<? extends Positionable> items) {
        if (lastPosition.isPresent() && lastPosition.get() >= 1000) {
            return reorderAndGetNextPosition(items);
        }

        return lastPosition
                .map(Position::of)
                .map(Position::next)
                .orElseGet(Position::first);
    }

    /**
     * 마지막 position이 상한을 넘은 경우 해당 메서드 호출 후 재정렬 (Lazy 처리)
     * 재정렬 후 마지막 위치 +1의 값 반환
     * @param items
     * @return
     */
    public Position reorderAndGetNextPosition(List<? extends Positionable> items) {
        for (int order=0, end=items.size(); order < end; order++) {
            items.get(order).movePosition(new Position(order + 1));
        }

        return new Position(items.size() + 1);
    }
}
