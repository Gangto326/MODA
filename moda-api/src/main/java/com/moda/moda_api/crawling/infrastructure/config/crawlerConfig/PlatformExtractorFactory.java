package com.moda.moda_api.crawling.infrastructure.config.crawlerConfig;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.moda.moda_api.crawling.domain.model.UrlDomainType;

@Component
public class PlatformExtractorFactory {
	private static final Map<String, ExtractorConfig> CONFIGS = new HashMap<>();

	static {
		//defalut값들.. 미분류 url..
		CONFIGS.put("default", ExtractorConfig.builder()
			.pattern("default")
			.contentSelector("body") // 일반적으로 본문은 body 태그에 있음
			.imageSelector("img") // 모든 이미지 태그를 선택
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.UNCLASSIFIED)
			.build());

		// 네이버 블로그 설정
		CONFIGS.put("blog.naver.com", ExtractorConfig.builder()
			.pattern("blog.naver.com")
			.contentSelector(".se-main-container")
			.imageSelector(".se-image img, .se-module-image img")
			.requiresFrame(true)
			.frameId("mainFrame")
			.urlDomainType(UrlDomainType.NAVER_BLOG)
			.build());

		// 네이버 뉴스 설정
		CONFIGS.put("news.naver.com", ExtractorConfig.builder()
			.pattern("news.naver.com")
			.contentSelector("#dic_area, .go_trans._article_content")
			.imageSelector("#dic_area img, .go_trans._article_content img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.NAVER_NEWS)
			.build());

		// Tistory 설정
		CONFIGS.put("tistory.com", ExtractorConfig.builder()
			.pattern(".tistory.com")
			.contentSelector(".contents_style")
			.imageSelector(".contents_style img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.TISTORY)
			.build());

		// Velog 설정
		CONFIGS.put("velog.io", ExtractorConfig.builder()
			.pattern("velog.io")
			.contentSelector(".sc-dFtzxp")
			.imageSelector(".sc-dFtzxp img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.VELOG)
			.build());

		// 다음 뉴스 설정
		CONFIGS.put("v.daum.net", ExtractorConfig.builder()
			.pattern("v.daum.net")
			.contentSelector(".article_view")
			.imageSelector(".article_view img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.DAUM_NEWS)
			.build());

		// 서치 설정.
		CONFIGS.put("google.com", ExtractorConfig.builder()
			.pattern("google.com")
			// 더 구체적이고 정확한 셀렉터 사용
			.contentSelector("div.MjjYud a[jsname='UWckNb']")
			.urlSelector("div.MjjYud a[jsname='UWckNb'][href]")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.GOOGLE_SEARCH)
			.build());
	}

	// 하나씩 꺼내보면서 맞는 사이트가 있는지 찾아보는 과정
	public ExtractorConfig getConfig(String url) {
		return CONFIGS.entrySet().stream()
			.filter(entry -> url.contains(entry.getKey()))
			.map(Map.Entry::getValue)
			.findFirst()
			.orElse(CONFIGS.get("default")); // 선택되지 않았다면 defalut 방법으로 크롤링
	}

}