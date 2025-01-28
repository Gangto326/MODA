package com.moda.moda_api.board.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "read_boards")
@IdClass(ReadBoardId.class)
public class ReadBoardEntity {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "board_id")
    private String boardId;
}
