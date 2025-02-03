package com.moda.moda_api.summary.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CardSummaryResponse {
	Integer typeId;
	String title;
	String content;
	String thumbnailContent;
	String thumbnailUrl;

}
