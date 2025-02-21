package com.moda.moda_api.category.domain;

import com.moda.moda_api.category.exception.PositionUpdateException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryPositionService {



    /**
     * sourcePosition과 targetPosition의 대소관계를 비교하여
     * 변경 대상인 item의 position을 +1 또는 -1 처리
     *
     * @param items
     */
    public void adjustPositions(List<? extends Positionable> items, Position source, Position target) {
        if (source.getValue().equals(target.getValue())) {
            throw new PositionUpdateException("같은 위치로는 이동할 수 없습니다.");
        }
        if (target.getValue() == 0) {
            throw new PositionUpdateException("0번 자리로는 이동할 수 없습니다.");
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
     * 카테고리 리스트를 Position 값 기준으로 정렬
     * @param items
     * @return
     */
    public <T extends Positionable> List<T> sortByPosition(List<T> items) {
        return items.stream()
                .sorted(Comparator.comparing(item -> item.getPosition().getValue()))
                .collect(Collectors.toList());
    }
}
