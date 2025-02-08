package com.moda.moda_api.crawling.application.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.stereotype.Service;

import com.moda.moda_api.crawling.domain.model.Url;
import com.moda.moda_api.crawling.infrastructure.crawl.AbstractExtractor;
import com.moda.moda_api.crawling.infrastructure.repository.UrlRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final AbstractExtractor abstractExtractor;
	private final UrlRedisRepository urlRedisRepository;

	public List<Url> crawlByKeyWord(String keyword) {
		System.out.println("검색 URL 생성 ");
		// 1. Google 검색 URL 생성
		String searchUrl = buildGoogleSearchUrl(keyword);

		System.out.println(searchUrl);
		System.out.println("크롤링 수행");
		// 2. 크롤링 수행
		List<Url> crawledUrls = abstractExtractor.extractUrl(searchUrl);

		// 3. Redis에 저장 (keyword를 key로 사용)
		urlRedisRepository.saveUrls(keyword, crawledUrls);

		List<Url> urls = urlRedisRepository.getUrls(keyword);
		return urls;
	}

	private String buildGoogleSearchUrl(String keyword) {
		try {
			StringBuilder searchUrlBuilder = new StringBuilder();

			// Google 검색 기본 URL
			searchUrlBuilder.append("https://www.google.com/search");
			searchUrlBuilder.append("?q=").append(URLEncoder.encode(keyword, "UTF-8"));

			// 추가 매개변수
			searchUrlBuilder.append("&num=20");  // 검색 결과 수 (최대 100)
			searchUrlBuilder.append("&hl=ko");   // 검색 언어 설정 (한국어)
			searchUrlBuilder.append("&safe=active"); // SafeSearch 필터
			searchUrlBuilder.append("&start=0");  // 검색 시작 위치 (페이징)

			return searchUrlBuilder.toString();

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("키워드 인코딩 실패: " + keyword, e);
		}
	}

}
