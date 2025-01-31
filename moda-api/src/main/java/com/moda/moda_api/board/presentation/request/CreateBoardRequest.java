package com.moda.moda_api.board.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardRequest {
    private String title;
    private Boolean isPublic = true;
}
