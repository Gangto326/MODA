package com.moda.moda_api.summary.infrastructure.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AiYoutubeRequestDTO {
	List<TitleAndContent> paragraph;
}

