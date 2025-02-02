package com.moda.moda_api.board.domain;

import com.moda.moda_api.user.domain.UserId;
import lombok.Value;

@Value
public class ReadBoard {
    UserId userId;
    BoardId boardId;
}
