package com.moda.moda_api.summary.infrastructure.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LilysSummary {
	private Integer typeId;
	private List<TitleAndContent> contents;
	private String thumbnailContent;
	private String thumbnailUrl;
	private String title;
	private String[] timeStamp;
}
