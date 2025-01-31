package com.moda.moda_api.user.domain;


import com.moda.moda_api.user.exception.InvalidUserIdException;
import lombok.Value;

/**
 * user 도메인과 병합 전 임시 class
 */
@Value
public class UserId {
    String value;

    public UserId(String value) {
        validateUserId(value);
        this.value = value;
    }

    /**
     * UserId 값 검증
     */
    private void validateUserId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidUserIdException("유저 ID가 존재하지 않습니다.");
        }
    }
}
