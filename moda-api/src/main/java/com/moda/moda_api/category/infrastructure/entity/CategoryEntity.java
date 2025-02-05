package com.moda.moda_api.category.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "category")
public class CategoryEntity {
    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category", nullable = false)
    private String category;
}
