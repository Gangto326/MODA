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

	public CompletableFuture<SummaryResultDto> getSummary(String url) throws Exception {
		//youtube일 경우
		if(url.contains("youtube.com")){
			return lilysSummaryService.summarize(url);
		}
		//webSite일경우
		return crawlingSummaryService.summarize(url);
	}
}