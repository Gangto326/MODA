package com.moda.moda_api.summary.domain.service;

import java.util.List;

public interface ContentExtractorService {
	String extractTitle(String url);

	String extractContent(String url);

	List<String> extractImages(String url);
}
