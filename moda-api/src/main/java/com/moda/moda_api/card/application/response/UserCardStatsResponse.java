package com.moda.moda_api.card.application.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCardStatsResponse {
	private String nickname;
	private String allCount;
	private String bookmarkCount;
}
