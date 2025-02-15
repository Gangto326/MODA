package com.moda.moda_api.crawling.infrastructure.config.crawlerConfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.moda.moda_api.crawling.domain.model.UrlDomainType;

@Component
public class PlatformExtractorFactory {
	private static final List<ExtractorConfig> CONFIGS = new ArrayList<>();

	//////////////////////////////////////////////////////////////////////////////
	// 무조건 순서는 모바일 먼저다!!!!!!!!!!!!!///////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////

	static {

		CONFIGS.add(ExtractorConfig.builder()
			.pattern("m.blog.naver.com")
			.contentSelector(".se-main-container")
			.imageSelector(".se_mediaImage")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.NAVER_BLOG)
			.build());

		CONFIGS.add(ExtractorConfig.builder()
			.pattern(".tistory.com/m")
			.contentSelector(".blogview_content")
			.imageSelector(".blogview_content img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.TISTORY)
			.build());

		//////////////////////////////////////////////////////////////
		////////////여기 위로는 모바일 밑으로는 pc////////////////////////
		/////////////////////////////////////////////////////////////

		CONFIGS.add(ExtractorConfig.builder()
			.pattern("blog.naver.com")
			.contentSelector(".se-main-container")
			.imageSelector(".se-image img, .se-module-image img")
			.requiresFrame(true)
			.frameId("mainFrame")
			.urlDomainType(UrlDomainType.NAVER_BLOG)
			.build());
		CONFIGS.add(ExtractorConfig.builder()
			.pattern(".tistory.com")
			.contentSelector(".contents_style")
			.imageSelector(".contents_style img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.TISTORY)
			.build());

		CONFIGS.add(ExtractorConfig.builder()
			.pattern("n.news.naver.com")
			.contentSelector("#dic_area, .go_trans._article_content")
			.imageSelector("#dic_area img, .go_trans._article_content img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.NAVER_NEWS)
			.build()
		);

		// Tistory 설정
		CONFIGS.add(ExtractorConfig.builder()
			.pattern(".tistory.com")
			.contentSelector(".contents_style")
			.imageSelector(".contents_style img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.TISTORY)
			.build());

		// Velog 설정
		CONFIGS.add(ExtractorConfig.builder()
			.pattern("velog.io")
			.contentSelector(".sc-dFtzxp")
			.imageSelector(".sc-dFtzxp img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.VELOG)
			.build());

		// 다음 뉴스 설정
		CONFIGS.add(ExtractorConfig.builder()
			.pattern("v.daum.net")
			.contentSelector(".article_view")
			.imageSelector(".article_view img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.DAUM_NEWS)
			.build());

		// 서치 설정.
		CONFIGS.add(ExtractorConfig.builder()
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
		String lowerUrl = url.toLowerCase();

		return CONFIGS.stream()
			.filter(config -> lowerUrl.contains(config.getPattern()))
			.findFirst()
			.orElse(getDefaultConfig());
	}

	private ExtractorConfig getDefaultConfig() {
		return ExtractorConfig.builder()
			.pattern("default")
			.contentSelector("body")
			.imageSelector("img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.UNCLASSIFIED)
			.build();
	}

}
