package com.moda.moda_api.board.infrastructure.entity;

import com.moda.moda_api.user.infrastructure.entity.UserEntity;
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

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @MapsId("boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;
}
