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
    @Column(name = "category_id", updatable = false)
    private Long categoryId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "position")
    private Integer position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity categoryEntity;

    public void setPosition(Integer position) {
        this.position = position;
    }
}
