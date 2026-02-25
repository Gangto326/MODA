import re
import logging
from dataclasses import dataclass
from typing import Optional
from urllib.parse import urljoin

import httpx
from bs4 import BeautifulSoup

logger = logging.getLogger(__name__)

USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
TIMEOUT = 15.0


@dataclass
class CrawlResult:
    title: str
    text: str
    images: list[str]
    domain_type: str


@dataclass
class SsrPlatform:
    pattern: str
    content_selector: str
    requires_iframe: bool
    iframe_id: Optional[str]
    domain_type: str


SSR_PLATFORMS = [
    SsrPlatform("m.blog.naver.com",
                 ".se_component_wrap.__se_component_area, .se-main-container, .post_ct, .se_doc_viewer",
                 False, None, "NAVER_BLOG"),
    SsrPlatform("blog.naver.com",
                 ".se-main-container, .se_component_wrap, .post_ct",
                 True, "mainFrame", "NAVER_BLOG"),
    SsrPlatform(".tistory.com/m",
                 ".blogview_content",
                 False, None, "TISTORY"),
    SsrPlatform(".tistory.com",
                 ".contents_style",
                 False, None, "TISTORY"),
    SsrPlatform(".brunch",
                 ".wrap_body_frame",
                 False, None, "BRUNCH"),
    SsrPlatform("n.news.naver.com",
                 "#dic_area, #newsct_article",
                 False, None, "NAVER_NEWS"),
    SsrPlatform("v.daum.net",
                 ".article_view",
                 False, None, "DAUM_NEWS"),
    SsrPlatform("m.sports.naver.com",
                 "._article_content, #newsEndContents",
                 False, None, "NAVER_SPORTS"),
    SsrPlatform("sports.naver.com",
                 "._article_content, #newsEndContents",
                 False, None, "NAVER_SPORTS"),
]

VELOG_URL_PATTERN = re.compile(r"velog\.io/@([^/]+)/(.+?)(?:\?.*)?$")

IMAGE_URL_PATTERN = re.compile(r".*\.(jpg|jpeg|png|gif|webp)($|\?.*)")


def _is_valid_image_url(url: str) -> bool:
    return bool(IMAGE_URL_PATTERN.match(url)) and "favicon" not in url and "logo" not in url and len(url) > 10


def _extract_images(element, base_url: str) -> list[str]:
    if element is None:
        return []
    images = []
    for img in element.find_all("img"):
        src = img.get("src", "")
        if not src:
            src = img.get("data-src", "")
        if src and not src.startswith("http"):
            src = urljoin(base_url, src)
        if src and _is_valid_image_url(src):
            images.append(src)
    # 첫 번째 이미지 건너뛰기 (기존 로직 유지)
    return images[1:] if len(images) > 1 else []


def _select_content(soup: BeautifulSoup, selector_group: str):
    for selector in selector_group.split(","):
        selector = selector.strip()
        el = soup.select_one(selector)
        if el and el.get_text(strip=True):
            return el
    logger.warning("[Crawler] All selectors failed, falling back to body")
    return soup.body


class SsrCrawler:
    def crawl(self, url: str, platform: SsrPlatform) -> CrawlResult:
        logger.info("[SSR] Crawling: %s (platform: %s)", url, platform.domain_type)
        headers = {"User-Agent": USER_AGENT}

        with httpx.Client(timeout=TIMEOUT, follow_redirects=True, headers=headers) as client:
            if platform.requires_iframe:
                soup = self._crawl_naver_blog_desktop(client, url, platform)
            else:
                resp = client.get(url)
                resp.raise_for_status()
                soup = BeautifulSoup(resp.text, "lxml")

        title = soup.title.string.strip() if soup.title and soup.title.string else ""
        content_el = _select_content(soup, platform.content_selector)
        text = content_el.get_text(separator=" ", strip=True) if content_el else ""
        images = _extract_images(content_el, url)

        logger.info("[SSR] Crawled %s — text length: %d, images: %d",
                     platform.domain_type, len(text), len(images))

        return CrawlResult(title=title, text=text, images=images, domain_type=platform.domain_type)

    def _crawl_naver_blog_desktop(self, client: httpx.Client, url: str, platform: SsrPlatform) -> BeautifulSoup:
        resp = client.get(url)
        resp.raise_for_status()
        main_soup = BeautifulSoup(resp.text, "lxml")

        iframe = main_soup.select_one(f"iframe#{platform.iframe_id}")
        if iframe is None:
            logger.warning("[SSR] No %s iframe found, using main document", platform.iframe_id)
            return main_soup

        iframe_src = iframe.get("src", "")
        if not iframe_src.startswith("http"):
            iframe_src = urljoin("https://blog.naver.com", iframe_src)

        logger.info("[SSR] Naver blog iframe URL: %s", iframe_src)
        resp2 = client.get(iframe_src)
        resp2.raise_for_status()
        return BeautifulSoup(resp2.text, "lxml")


class VelogCrawler:
    GRAPHQL_URL = "https://v2.velog.io/graphql"
    QUERY = """
        query ReadPost($username: String!, $url_slug: String!) {
          post(username: $username, url_slug: $url_slug) {
            title
            body
            thumbnail
          }
        }
    """

    def crawl(self, url: str) -> CrawlResult:
        match = VELOG_URL_PATTERN.search(url)
        if not match:
            raise ValueError(f"Invalid Velog URL format: {url}")

        username = match.group(1)
        slug = match.group(2)
        logger.info("[Velog GraphQL] Fetching post: @%s/%s", username, slug)

        with httpx.Client(timeout=TIMEOUT) as client:
            resp = client.post(
                self.GRAPHQL_URL,
                json={
                    "query": self.QUERY,
                    "variables": {"username": username, "url_slug": slug},
                },
                headers={"Content-Type": "application/json"},
            )
            resp.raise_for_status()
            data = resp.json()

        post = data.get("data", {}).get("post")
        if post is None:
            raise ValueError(f"Velog post not found: {url}")

        title = post.get("title", "")
        body = post.get("body", "")
        thumbnail = post.get("thumbnail")

        # 마크다운 본문에서 텍스트만 추출
        text = body
        text = re.sub(r"```[\s\S]*?```", " ", text)         # 코드 블록 제거
        text = re.sub(r"`[^`]+`", " ", text)                 # 인라인 코드 제거
        text = re.sub(r"!?\[([^\]]*)\]\([^)]+\)", r"\1", text)  # 링크/이미지 마크다운 제거
        text = re.sub(r"[#*>~_|\-]", " ", text)              # 마크다운 기호 제거
        text = re.sub(r"\s+", " ", text).strip()             # 연속 공백 정리

        images = [thumbnail] if thumbnail else []
        logger.info("[Velog GraphQL] Fetched: title='%s', text length=%d", title, len(text))

        return CrawlResult(title=title, text=text, images=images, domain_type="VELOG")


class FallbackCrawler:
    def crawl(self, url: str) -> CrawlResult:
        logger.info("[Fallback] Crawling: %s", url)
        headers = {"User-Agent": USER_AGENT}

        with httpx.Client(timeout=TIMEOUT, follow_redirects=True, headers=headers) as client:
            resp = client.get(url)
            resp.raise_for_status()

        soup = BeautifulSoup(resp.text, "lxml")
        title = soup.title.string.strip() if soup.title and soup.title.string else ""
        body = soup.body
        text = body.get_text(separator=" ", strip=True) if body else ""
        images = _extract_images(body, url)

        logger.info("[Fallback] Crawled — text length: %d, images: %d", len(text), len(images))
        return CrawlResult(title=title, text=text, images=images, domain_type="UNCLASSIFIED")


class CrawlerResolver:
    def __init__(self):
        self.ssr_crawler = SsrCrawler()
        self.velog_crawler = VelogCrawler()
        self.fallback_crawler = FallbackCrawler()

    def crawl(self, url: str) -> CrawlResult:
        lower = url.lower()

        # 1. Velog
        if "velog.io/@" in lower:
            logger.info("[Resolver] URL '%s' → VelogCrawler", url)
            return self.velog_crawler.crawl(url)

        # 2. SSR 플랫폼
        for platform in SSR_PLATFORMS:
            if platform.pattern in lower:
                logger.info("[Resolver] URL '%s' → SsrCrawler (%s)", url, platform.domain_type)
                return self.ssr_crawler.crawl(url, platform)

        # 3. 폴백
        logger.info("[Resolver] URL '%s' → FallbackCrawler", url)
        return self.fallback_crawler.crawl(url)
