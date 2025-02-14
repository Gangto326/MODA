package com.moda.moda_api.summary.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TitleAndContent {
	private String title;
	private String content;
}
