package com.moda.moda_api.summary.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateSummaryCommand {
	private String url;
	private String result_type;
}
