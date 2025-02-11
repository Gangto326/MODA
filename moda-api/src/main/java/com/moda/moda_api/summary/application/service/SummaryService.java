package com.moda.moda_api.summary.application.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.moda.moda_api.summary.application.dto.SummaryResultDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SummaryService {
	private final CrawlingSummaryService crawlingSummaryService;
	private final LilysSummaryService lilysSummaryService;

	private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 적절한 스레드 풀 설정

	public CompletableFuture<SummaryResultDto> getSummaryAsync(String url) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return getSummary(url); // 기존 동기 메서드를 비동기 실행
			} catch (Exception e) {
				throw new RuntimeException(e); // 예외를 CompletableFuture에서 정상 처리할 수 있도록 감싸기
			}
		}, executorService);
	}

	public SummaryResultDto getSummary(String url) throws Exception {
		// YouTube일 경우
		if (url.contains("youtube.com")) {
			return lilysSummaryService.summarize(url);
		}
		// 일반 웹사이트일 경우
		return crawlingSummaryService.summarize(url);
	}

}