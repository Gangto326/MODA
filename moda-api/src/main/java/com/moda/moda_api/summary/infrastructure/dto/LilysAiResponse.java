package com.moda.moda_api.summary.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class LilysAiResponse {
	private String requestId;
	private String status;
}