package com.moda.moda_api.crawling.infrastructure.config.crawlerConfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.moda.moda_api.crawling.domain.model.UrlDomainType;

@Component
public class PlatformExtractorFactory {
	private static final List<ExtractorConfig> CONFIGS = initializeConfigs();

	private static List<ExtractorConfig> initializeConfigs() {
		List<ExtractorConfig> configs = new ArrayList<>();

		// Mobile configurations
		configs.add(createMobileNaverBlogConfig());
		configs.add(createMobileTistoryConfig());
		configs.add(createNaverNewsConfig());
		configs.add(createMobileSportsNaverConfig());

		// Desktop configurations
		configs.add(createDesktopBrunchConfig());
		configs.add(createDesktopNaverBlogConfig());
		configs.add(createDesktopTistoryConfig());
		configs.add(createNaverNewsConfig());
		configs.add(createVelogConfig());
		configs.add(createDaumNewsConfig());
		configs.add(createGoogleSearchConfig());
		configs.add(createNamuwikiConfig());

		// configs.add(createNamuwikiConfig()); // Added Namuwiki configuration

		return configs;
	}
	//////////////////////////////////////////////////////////////////////////////
	// 무조건 순서는 모바일 먼저다!!!!!!!!!!!!!///////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////

	private static ExtractorConfig createMobileNaverBlogConfig() {
		return ExtractorConfig.builder()
			.pattern("m.blog.naver.com")
			.contentSelector(".se_component_wrap.__se_component_area, .se-main-container, .post_ct,.se_doc_viewer.se_body_wrap.se_theme_transparent")
			.imageSelector(".se_mediaImage")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.NAVER_BLOG)
			.build();
	}

	private static ExtractorConfig createMobileTistoryConfig() {
		return ExtractorConfig.builder()
			.pattern(".tistory.com/m")
			.contentSelector(".blogview_content")
			.imageSelector(".blogview_content img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.TISTORY)
			.build();
	}

	private static ExtractorConfig createMobileSportsNaverConfig() {
		return ExtractorConfig.builder()
			.pattern("m.sports.naver.com")
			.contentSelector("._article_content")
			.imageSelector("img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.NAVER_SPORTS)
			.build();
	}

	//////////////////////////////////////////////////////////////
	////////////여기 위로는 모바일 밑으로는 pc////////////////////////
	/////////////////////////////////////////////////////////////
	private static ExtractorConfig createDesktopBrunchConfig() {
		return ExtractorConfig.builder()
			.pattern(".brunch")
			.contentSelector(".wrap_body_frame")
			.imageSelector(".contents_style img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.BRUNCH)
			.build();
	}

	private static ExtractorConfig createDesktopNaverBlogConfig() {
		return ExtractorConfig.builder()
			.pattern("blog.naver.com")
			.contentSelector(".se-main-container, se_component_wrap.sect_dsc __se_component_area, .post_ct")
			.imageSelector(".se-image img, .se-module-image img")
			.requiresFrame(true)
			.frameId("mainFrame")
			.urlDomainType(UrlDomainType.NAVER_BLOG)
			.build();
	}

	private static ExtractorConfig createDesktopTistoryConfig() {
		return ExtractorConfig.builder()
			.pattern(".tistory.com")
			.contentSelector(".contents_style")
			.imageSelector(".contents_style img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.TISTORY)
			.build();
	}

	private static ExtractorConfig createNaverNewsConfig() {
		return ExtractorConfig.builder()
			.pattern("n.news.naver.com")
			.contentSelector("#newsct_article")
			.imageSelector("#img1")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.NAVER_NEWS)
			.build();
	}

	private static ExtractorConfig createVelogConfig() {
		return ExtractorConfig.builder()
			.pattern("velog.io")
			.contentSelector(".sc-dFtzxp")
			.imageSelector(".sc-dFtzxp img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.VELOG)
			.build();
	}

	private static ExtractorConfig createDaumNewsConfig() {
		return ExtractorConfig.builder()
			.pattern("v.daum.net")
			.contentSelector(".article_view")
			.imageSelector(".article_view img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.DAUM_NEWS)
			.build();
	}

	private static ExtractorConfig createGoogleSearchConfig() {
		return ExtractorConfig.builder()
			.pattern("google.com")
			.contentSelector("div.MjjYud a[jsname='UWckNb']")
			.urlSelector("div.MjjYud a[jsname='UWckNb'][href]")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.GOOGLE_SEARCH)
			.build();
	}

	private static ExtractorConfig createDefaultConfig() {
		return ExtractorConfig.builder()
			.pattern("default")
			.contentSelector("body")
			.imageSelector("img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.UNCLASSIFIED)
			.build();
	}

	private static ExtractorConfig createNamuwikiConfig() {
		return ExtractorConfig.builder()
			.pattern("namu.wiki")
			.contentSelector(".KT5SIFMZ")
			.imageSelector("div.wiki-content img")
			.requiresFrame(false)
			.urlDomainType(UrlDomainType.NAMUWIKI)
			.build();
	}



	// 하나씩 꺼내보면서 맞는 사이트가 있는지 찾아보는 과정
	public ExtractorConfig getConfig(String url) {
		String lowerUrl = url.toLowerCase();

		return CONFIGS.stream()
			.filter(config -> lowerUrl.contains(config.getPattern()))
			.findFirst()
			.orElse(createDefaultConfig());
	}

}
