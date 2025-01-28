package com.moda.moda_api.board.domain;

import com.moda.moda_api.board.exception.InvalidBoardIdException;
import lombok.Value;

/**
 * BoardId 식별자 Value Object 생성을 위한 Domain
 */
@Value
public class BoardId {
    String value;

    public BoardId(String value) {
        validateBoardId(value);
        this.value = value;
    }

    /**
     * BoardId 값 검증
     */
    private void validateBoardId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidBoardIdException("보드 ID가 존재하지 않습니다.");
        }
    }
}
