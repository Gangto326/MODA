package com.moda.moda_api.category.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "category_order")
@IdClass(CategoryOrderId.class)
public class CategoryOrderEntiy {
    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "position")
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;
}
