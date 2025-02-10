package com.moda.moda_api.category.infrastructure.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class CategoryOrderId implements Serializable {
    private Long categoryId;
    private String userId;
}
