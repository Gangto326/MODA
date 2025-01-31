package com.moda.moda_api.summary.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrawlingResponse {
	private String title;
	private String content;
	private List<String> imageUrls;
	private LocalDateTime crawledAt;
	private String message;
}