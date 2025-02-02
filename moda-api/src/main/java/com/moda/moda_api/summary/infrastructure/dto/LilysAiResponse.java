package com.moda.moda_api.summary.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@Getter
public class LilysAiResponse {
	private String requestId;
}