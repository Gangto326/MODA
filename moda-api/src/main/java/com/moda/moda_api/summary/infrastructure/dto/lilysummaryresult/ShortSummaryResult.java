package com.moda.moda_api.summary.infrastructure.dto.lilysummaryresult;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortSummaryResult {
	private OuterData data;
	private String status;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OuterData {
		private String type;
		private String resultLanguage;
		private NestedData data;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class NestedData {
		private String summary;
	}
}
