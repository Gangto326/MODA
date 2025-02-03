package com.moda.moda_api.board.application.response;

import com.moda.moda_api.card.application.response.CardListResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponse {
    private BoardResponse board;
    private List<CardListResponse> recentCards;
    private Boolean isRead;
}
