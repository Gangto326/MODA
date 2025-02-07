package com.moda.moda_api.crawling.domain.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchParameters {
	private String keyword;
	private String site;  // 특정 사이트 내 검색
	private String fileType; // 파일 타입 (pdf, doc 등)
	private DateRange dateRange; // 날짜 범위
	private boolean exactMatch; // 정확한 문구 검색
	private List<String> excludeTerms; // 제외할 검색어

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DateRange {
		private LocalDate from;
		private LocalDate to;
	}
}