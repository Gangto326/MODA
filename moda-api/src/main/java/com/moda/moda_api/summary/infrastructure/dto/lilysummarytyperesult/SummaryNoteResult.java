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
public class SummaryNoteResult {
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
		private List<SummaryNoteEntry> summaryNote;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class SummaryNoteEntry {
		private double timestamp;
		private String title;
		private List<String> content;
		private String originalScript;
	}
}
