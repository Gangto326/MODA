import asyncio
import logging

from app.schemas.crawl import CrawlResponse
from app.services.crawler import CrawlerResolver
from app.services.post_summary import PostSummary

logger = logging.getLogger(__name__)


class CrawlAndAnalyze:
    def __init__(self, url: str):
        self.url = url
        self.resolver = CrawlerResolver()
        # 크롤링 결과
        self.title = ""
        self.text = ""
        self.images: list[str] = []
        self.domain_type = ""
        self.thumbnail_url = None

    async def execute(self):
        # 1단계: 크롤링
        await asyncio.to_thread(self._crawl)

        if not self.text:
            raise ValueError(f"Failed to extract text from: {self.url}")

        # 2단계: AI 분석 (기존 PostSummary 재사용)
        summarizer = PostSummary(self.text)
        await summarizer.execute()

        # 결과 조합
        self._category_id = summarizer.category_id
        self._content = summarizer.content
        self._keywords = summarizer.keywords
        self._thumbnail_content = summarizer.thumbnail_content
        self._embedding_vector = summarizer.embedding_vector

    def _crawl(self):
        result = self.resolver.crawl(self.url)
        self.title = result.title
        self.text = result.text
        self.images = result.images
        self.domain_type = result.domain_type
        self.thumbnail_url = result.images[0] if result.images else None

    def get_response(self) -> CrawlResponse:
        return CrawlResponse(
            category_id=self._category_id,
            content=self._content,
            keywords=self._keywords,
            thumbnail_content=self._thumbnail_content,
            embedding_vector=self._embedding_vector,
            title=self.title,
            thumbnail_url=self.thumbnail_url,
            images=self.images,
            domain_type=self.domain_type,
        )
