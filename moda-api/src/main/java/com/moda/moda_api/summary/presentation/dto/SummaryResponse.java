package com.moda.moda_api.summary.presentation.dto;


public class SummaryResponse {
	private String summary;

	public SummaryResponse(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return summary;
	}
}