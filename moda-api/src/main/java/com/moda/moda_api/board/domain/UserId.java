package com.moda.moda_api.board.domain;

import lombok.Value;

/**
 * user 도메인과 병합 전 임시 class
 */
@Value
public class UserId {
    String value;

    public UserId(String value) {
        this.value = value;
    }
}
