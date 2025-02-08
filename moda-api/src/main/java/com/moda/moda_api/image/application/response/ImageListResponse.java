package com.moda.moda_api.image.application.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageListResponse {
	private String imageId;
	private Long categoryId;
	private String[] keywords;
	private String imageUrl;
	private LocalDateTime createdAt;
}
