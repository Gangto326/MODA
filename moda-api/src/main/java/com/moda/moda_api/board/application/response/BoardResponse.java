package com.moda.moda_api.board.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
    private String id;
    private String userId;
    private String title;
    private Integer position;
    private boolean isPublic;
    private LocalDateTime createdAt;
}
