package com.moda.moda_api.summary.presentation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO 클래스
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequest {
	private String url;
	private String resultType;
}