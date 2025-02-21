package com.moda.moda_api.crawling.domain.service;

import com.moda.moda_api.crawling.domain.model.UrlDomainType;

	public class UrlContentTypeResolver {

	public static UrlDomainType resolve(String url) {
		if (url.contains("tistory.com")) return UrlDomainType.TISTORY;
		if (url.contains("m.blog.naver.com")) return UrlDomainType.NAVER_BLOG;
		if (url.contains("blog.naver.com")) return UrlDomainType.NAVER_BLOG;
		if (url.contains("velog.io")) return UrlDomainType.VELOG;
		if (url.contains("news.naver.com")) return UrlDomainType.NAVER_NEWS;
		if (url.contains("news.daum.net")) return UrlDomainType.DAUM_NEWS;
		if (url.contains("youtube.com") || url.contains("youtu.be")) return UrlDomainType.YOUTUBE;
		if (url.contains("namu.wiki")) return UrlDomainType.NAMUWIKI;
		if (url.contains("brunch.co.kr")) return UrlDomainType.BRUNCH;
		if (url.contains("m.sports.naver.com")) return UrlDomainType.MOBILESPORTS;
		if (url.contains("sports.naver.com")) return UrlDomainType.NAVER_SPORTS;

		return UrlDomainType.UNCLASSIFIED;
	}

}