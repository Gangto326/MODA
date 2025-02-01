package com.moda.moda_api.board.infrastructure.entity;

import com.moda.moda_api.user.infrastructure.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "boards")
public class BoardEntity {
    @Id
    @Column(name = "board_id")
    private String boardId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = true;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "created__at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
