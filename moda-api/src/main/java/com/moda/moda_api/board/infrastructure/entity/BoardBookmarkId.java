package com.moda.moda_api.board.infrastructure.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class BoardBookmarkId implements Serializable {
    private String userId;
    private String boardId;
}
