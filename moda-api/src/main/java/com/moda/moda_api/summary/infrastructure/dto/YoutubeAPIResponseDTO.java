package com.moda.moda_api.summary.infrastructure.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class YoutubeAPIResponseDTO {
	private String[] tags;
	private String description;
	private String channelTitle;
	private String channelThumbnailUrl;

	@Builder
	public YoutubeAPIResponseDTO(String[] tags, String description,
		String channelTitle, String channelThumbnailUrl) {
		this.tags = tags;
		this.description = description;
		this.channelTitle = channelTitle;
		this.channelThumbnailUrl = channelThumbnailUrl;
	}
}