package com.moda.moda_api.crawling.presentation.dto;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;

@Getter
public class SearchRequest {
	private String keyword;
}
