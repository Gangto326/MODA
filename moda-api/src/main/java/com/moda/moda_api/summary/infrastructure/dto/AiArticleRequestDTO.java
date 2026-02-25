package com.moda.moda_api.summary.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AiArticleRequestDTO {
	private String content;
}
