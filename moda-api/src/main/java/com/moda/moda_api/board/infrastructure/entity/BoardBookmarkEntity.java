package com.moda.moda_api.board.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board_bookmarks",
        indexes = @Index(name = "idx_user_id", columnList = "user_id"))
@IdClass(BoardBookmarkId.class)
public class BoardBookmarkEntity {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "board_id")
    private String boardId;

    @Column(name = "position")
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private BoardEntity board;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    private UserEntity user;
}
