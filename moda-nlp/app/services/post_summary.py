import asyncio
import json

from app.constants.category import categories_name
from app.constants.post_prompt import make_summary_prompt, make_keywords_and_thumbnail_prompt, make_category_prompt
from app.schemas.post import PostResponse
from app.services.embedding import Embedding
from app.services.llm_client import LLMClient, CategoryResponse, KeywordAndThumbnailResponse


class PostSummary:
    MAX_CATEGORY_TRIES = 10

    def __init__(self, origin_content: str):
        self.origin_content = origin_content
        self.llm = LLMClient()
        self.embedder = Embedding()
        self.category = ''

        self.category_id = 0
        self.content = ''
        self.keywords = []
        self.thumbnail_content = ''
        self.embedding_vector = []

    #PostSummary 객체가 실행되면 가장 먼저 실행되는 함수
    async def execute(self):
        await asyncio.to_thread(self.choose_category)
        await asyncio.to_thread(self.summary_content)
        await asyncio.to_thread(self.make_keywords_and_thumbnail)
        await asyncio.to_thread(self.make_embedding_vector)

    #Response 형태로 만들어주는 함수
    def get_response(self) -> PostResponse:
        return PostResponse(
            category_id=self.category_id,
            content=self.content,
            keywords=self.keywords,
            thumbnail_content=self.thumbnail_content,
            embedding_vector=self.embedding_vector
        )

    #category를 선택하는 함수
    def choose_category(self):
        messages = make_category_prompt(self.origin_content)

        find_category = False
        attempt_count = 0
        while attempt_count < self.MAX_CATEGORY_TRIES:
            response = self.llm.chat_json(
                system=messages[0]['content'],
                user=messages[1]['content'],
                schema=CategoryResponse
            )

            for idx, category in enumerate(categories_name()):
                if category.lower() in response.lower():
                    find_category = True
                    self.category_id = idx + 1
                    self.category = category
                    break

            if find_category:
                break

            attempt_count += 1

        if attempt_count == self.MAX_CATEGORY_TRIES:
            self.category_id = 1
            self.category = 'All'

        print(f'카테고리: {self.category}')

    #origin_content를 요약하는 함수
    def summary_content(self):
        has_html_tag = any(tag in self.origin_content for tag in ['<h1>', '<h2>', '<h3>'])

        messages = make_summary_prompt(self.category, self.origin_content, has_html_tag)

        response = self.llm.chat(
            system=messages[0]['content'],
            user=messages[1]['content']
        )
        self.content = str(response).removeprefix("```markdown\n").removesuffix("```")

        print(f'요약본:\n{self.content}')

    #keywords와 thumbnail_content를 통합 생성하는 함수
    def make_keywords_and_thumbnail(self):
        messages = make_keywords_and_thumbnail_prompt(self.content)

        response = self.llm.chat_json(
            system=messages[0]['content'],
            user=messages[1]['content'],
            schema=KeywordAndThumbnailResponse
        )
        parsed = json.loads(response)
        self.keywords = [kw for kw in parsed['keyword'] if kw in self.content]
        self.thumbnail_content = parsed['thumbnail_content']

        print(f'키워드: {self.keywords}')
        print(f'썸네일 요약본: {self.thumbnail_content}')

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)
