package com.moda.moda_api.crawling.infrastructure.crawl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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
public class VelogGraphQLCrawler implements CrawlingStrategy {
	private final WebClient webClient;
	private final Timer velogTimer;
	private final Counter velogCounter;

	private static final String VELOG_GRAPHQL_URL = "https://v2.velog.io/graphql";
	// URL 패턴: velog.io/@{username}/{slug}
	private static final Pattern VELOG_URL_PATTERN = Pattern.compile("velog\\.io/@([^/]+)/(.+?)(?:\\?.*)?$");

	public VelogGraphQLCrawler(MeterRegistry registry) {
		this.webClient = WebClient.builder()
			.baseUrl(VELOG_GRAPHQL_URL)
			.defaultHeader("Content-Type", "application/json")
			.build();
		this.velogTimer = Timer.builder("moda.crawling.duration")
			.tag("strategy", "velog_graphql")
			.description("Velog GraphQL crawling duration")
			.register(registry);
		this.velogCounter = Counter.builder("moda.crawling.strategy")
			.tag("strategy", "velog_graphql")
			.description("Velog GraphQL crawling count")
			.register(registry);
	}

	@Override
	public boolean supports(String url) {
		return url.toLowerCase().contains("velog.io/@");
	}

	@Override
	public CrawledContent crawl(String url) {
		return velogTimer.record(() -> {
			velogCounter.increment();
			Matcher matcher = VELOG_URL_PATTERN.matcher(url);
			if (!matcher.find()) {
				throw new RuntimeException("Invalid Velog URL format: " + url);
			}

			String username = matcher.group(1);
			String slug = matcher.group(2);
			// URL-encoded slug를 그대로 사용
			log.info("[Velog GraphQL] Fetching post: @{}/{}", username, slug);

			String query = """
				query ReadPost($username: String!, $url_slug: String!) {
				  post(username: $username, url_slug: $url_slug) {
				    title
				    body
				    thumbnail
				  }
				}
				""";

			Map<String, Object> requestBody = Map.of(
				"query", query,
				"variables", Map.of("username", username, "url_slug", slug)
			);

			@SuppressWarnings("unchecked")
			Map<String, Object> response = webClient.post()
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(Map.class)
				.block(java.time.Duration.ofSeconds(15));

			if (response == null || !response.containsKey("data")) {
				throw new RuntimeException("Velog GraphQL returned no data for: " + url);
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) response.get("data");
			@SuppressWarnings("unchecked")
			Map<String, Object> post = (Map<String, Object>) data.get("post");

			if (post == null) {
				throw new RuntimeException("Velog post not found: " + url);
			}

			String title = (String) post.getOrDefault("title", "");
			String body = (String) post.getOrDefault("body", "");
			String thumbnail = (String) post.get("thumbnail");

			// 마크다운 본문에서 텍스트만 추출 (간단한 태그 제거)
			String text = body
				.replaceAll("```[\\s\\S]*?```", " ") // 코드 블록 제거
				.replaceAll("`[^`]+`", " ") // 인라인 코드 제거
				.replaceAll("!?\\[([^\\]]*)\\]\\([^)]+\\)", "$1") // 링크/이미지 마크다운 제거
				.replaceAll("[#*>~_|\\-]", " ") // 마크다운 기호 제거
				.replaceAll("\\s+", " ") // 연속 공백 정리
				.trim();

			String[] images = thumbnail != null ? new String[]{thumbnail} : new String[0];

			log.info("[Velog GraphQL] Fetched: title='{}', text length={}", title, text.length());

			return CrawledContent.builder()
				.url(new Url(url))
				.urlDomainType(UrlDomainType.VELOG)
				.title(title)
				.extractedContent(ExtractedContent.builder()
					.text(text)
					.images(images)
					.build())
				.build();
		});
	}
}
