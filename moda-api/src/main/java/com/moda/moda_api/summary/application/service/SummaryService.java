package com.moda.moda_api.summary.application.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.application.dto.SummaryResultDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SummaryService {
	private final CrawlingSummaryService crawlingSummaryService;
	private final LilysSummaryService lilysSummaryService;

	public CompletableFuture<SummaryResultDto> getSummary(String url, String userId) {
		// YouTube일 경우
		if (url.contains("youtube.com") || url.contains("youtu.be/") || url.contains("m.youtube.com") ) {
			return lilysSummaryService.summarize(url, userId);
		}
		// 일반 웹사이트일 경우
		return crawlingSummaryService.summarize(url, userId);
	}

}