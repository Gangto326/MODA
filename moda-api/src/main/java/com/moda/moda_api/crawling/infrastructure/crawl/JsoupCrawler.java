package com.moda.moda_api.crawling.infrastructure.crawl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.moda.moda_api.common.infrastructure.ImageStorageService;
import com.moda.moda_api.crawling.domain.model.CrawledContent;
import com.moda.moda_api.crawling.domain.model.ExtractedContent;
import com.moda.moda_api.crawling.domain.model.Url;
import com.moda.moda_api.crawling.domain.model.UrlDomainType;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JsoupCrawler implements CrawlingStrategy {
	private final ImageStorageService imageStorageService;
	private final Timer jsoupTimer;
	private final Counter jsoupCounter;

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
	private static final int TIMEOUT_MS = 15_000;

	private static final List<SsrPlatform> SSR_PLATFORMS = List.of(
		new SsrPlatform("m.blog.naver.com",
			".se_component_wrap.__se_component_area, .se-main-container, .post_ct, .se_doc_viewer",
			false, null, UrlDomainType.NAVER_BLOG),
		new SsrPlatform("blog.naver.com",
			".se-main-container, .se_component_wrap, .post_ct",
			true, "mainFrame", UrlDomainType.NAVER_BLOG),
		new SsrPlatform(".tistory.com/m",
			".blogview_content",
			false, null, UrlDomainType.TISTORY),
		new SsrPlatform(".tistory.com",
			".contents_style",
			false, null, UrlDomainType.TISTORY),
		new SsrPlatform(".brunch",
			".wrap_body_frame",
			false, null, UrlDomainType.BRUNCH),
		new SsrPlatform("n.news.naver.com",
			"#dic_area, #newsct_article",
			false, null, UrlDomainType.NAVER_NEWS),
		new SsrPlatform("v.daum.net",
			".article_view",
			false, null, UrlDomainType.DAUM_NEWS),
		new SsrPlatform("m.sports.naver.com",
			"._article_content, #newsEndContents",
			false, null, UrlDomainType.NAVER_SPORTS),
		new SsrPlatform("sports.naver.com",
			"._article_content, #newsEndContents",
			false, null, UrlDomainType.NAVER_SPORTS)
	);

	public JsoupCrawler(ImageStorageService imageStorageService, MeterRegistry registry) {
		this.imageStorageService = imageStorageService;
		this.jsoupTimer = Timer.builder("moda.crawling.duration")
			.tag("strategy", "jsoup")
			.description("Jsoup crawling duration")
			.register(registry);
		this.jsoupCounter = Counter.builder("moda.crawling.strategy")
			.tag("strategy", "jsoup")
			.description("Jsoup crawling count")
			.register(registry);
	}

	@Override
	public boolean supports(String url) {
		String lower = url.toLowerCase();
		return SSR_PLATFORMS.stream().anyMatch(p -> lower.contains(p.pattern));
	}

	@Override
	public CrawledContent crawl(String url) {
		return jsoupTimer.record(() -> {
			jsoupCounter.increment();
			String lower = url.toLowerCase();
			SsrPlatform platform = SSR_PLATFORMS.stream()
				.filter(p -> lower.contains(p.pattern))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No matching SSR platform for: " + url));

			log.info("[Jsoup] Crawling SSR site: {} (platform: {})", url, platform.domainType);

			try {
				Document doc;
				if (platform.requiresIframe) {
					doc = crawlNaverBlogDesktop(url, platform);
				} else {
					doc = Jsoup.connect(url)
						.userAgent(USER_AGENT)
						.timeout(TIMEOUT_MS)
						.followRedirects(true)
						.get();
				}

				String title = doc.title();
				Element contentElement = selectContent(doc, platform.contentSelector);
				String text = contentElement != null ? contentElement.text().trim() : "";

				String[] images = extractImages(contentElement);

				log.info("[Jsoup] Crawled {} in Jsoup — text length: {}, images: {}",
					platform.domainType, text.length(), images.length);

				return CrawledContent.builder()
					.url(new Url(url))
					.urlDomainType(platform.domainType)
					.title(title)
					.extractedContent(ExtractedContent.builder()
						.text(text)
						.images(images)
						.build())
					.build();
			} catch (IOException e) {
				log.error("[Jsoup] Failed to crawl {}: {}", url, e.getMessage());
				throw new RuntimeException("Jsoup crawling failed for: " + url, e);
			}
		});
	}

	private Document crawlNaverBlogDesktop(String url, SsrPlatform platform) throws IOException {
		// 네이버 블로그 데스크톱: iframe(mainFrame) 안에 실제 콘텐츠가 있음
		// PostView.naver URL로 직접 접근
		Document mainDoc = Jsoup.connect(url)
			.userAgent(USER_AGENT)
			.timeout(TIMEOUT_MS)
			.get();

		Element iframe = mainDoc.selectFirst("iframe#mainFrame");
		if (iframe == null) {
			log.warn("[Jsoup] No mainFrame iframe found, using main document");
			return mainDoc;
		}

		String iframeSrc = iframe.absUrl("src");
		if (iframeSrc.isEmpty()) {
			iframeSrc = iframe.attr("src");
			if (!iframeSrc.startsWith("http")) {
				iframeSrc = "https://blog.naver.com" + iframeSrc;
			}
		}

		log.info("[Jsoup] Naver blog iframe URL: {}", iframeSrc);
		return Jsoup.connect(iframeSrc)
			.userAgent(USER_AGENT)
			.timeout(TIMEOUT_MS)
			.get();
	}

	private Element selectContent(Document doc, String selectorGroup) {
		// 쉼표로 구분된 셀렉터를 순서대로 시도
		for (String selector : selectorGroup.split(",")) {
			Element el = doc.selectFirst(selector.trim());
			if (el != null && !el.text().isBlank()) {
				return el;
			}
		}
		// 모두 실패 시 body 폴백
		log.warn("[Jsoup] All selectors failed, falling back to body");
		return doc.body();
	}

	private String[] extractImages(Element contentElement) {
		if (contentElement == null) {
			return new String[0];
		}
		Elements imgs = contentElement.select("img");
		return imgs.stream()
			.map(img -> {
				String src = img.absUrl("src");
				if (src.isEmpty()) {
					src = img.attr("src");
				}
				return src;
			})
			.filter(src -> src != null && !src.isEmpty() && isValidImageUrl(src))
			.skip(1) // 첫 번째 이미지 건너뛰기 (기존 로직 유지)
			.map(src -> {
				try {
					return imageStorageService.uploadImageFromurl(src);
				} catch (Exception e) {
					log.error("[Jsoup] Failed to upload image: {}", src, e);
					return null;
				}
			})
			.filter(Objects::nonNull)
			.toArray(String[]::new);
	}

	private boolean isValidImageUrl(String url) {
		return url.matches(".*\\.(jpg|jpeg|png|gif|webp)($|\\?.*)") &&
			!url.contains("favicon") &&
			!url.contains("logo") &&
			url.length() > 10;
	}

	private record SsrPlatform(String pattern, String contentSelector,
							   boolean requiresIframe, String iframeId,
							   UrlDomainType domainType) {
	}
}
