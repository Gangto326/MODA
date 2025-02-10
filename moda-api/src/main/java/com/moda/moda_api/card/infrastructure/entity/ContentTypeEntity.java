package com.moda.moda_api.card.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "content_type")
public class ContentTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type", length = 100, nullable = false)
    private String type;

}