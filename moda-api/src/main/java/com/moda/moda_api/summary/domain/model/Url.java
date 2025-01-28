package com.moda.moda_api.summary.domain.model;

import lombok.Getter;

@Getter
public class Url {
    private final String value;

    public Url(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
