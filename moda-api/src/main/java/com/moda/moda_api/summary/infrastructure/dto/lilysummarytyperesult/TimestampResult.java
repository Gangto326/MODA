package com.moda.moda_api.summary.infrastructure.dto.lilysummarytyperesult;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimestampResult {
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
		private List<TimestampEntry> result;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class TimestampEntry {
		private String title;
		private double timestamp;
	}
}