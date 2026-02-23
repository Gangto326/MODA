import asyncio
import json

import googletrans
import requests

from app.constants.category import categories_name
from app.constants.image_prompt import make_analyze_prompt, make_category_prompt, make_keywords_content_prompt
from app.schemas.image import ImageResponse
from app.services.embedding import Embedding
from app.services.llm_client import LLMClient, CategoryResponse, KeywordResponse


class ImageAnalyze:
    MAX_CATEGORY_TRIES = 10

    def __init__(self, url: str):
        self.url = url
        self.image_bytes = b''
        self.llm = LLMClient()
        self.embedder = Embedding()
        self.category = ''

        self.category_id = 0
        self.content = ''
        self.keywords = []
        self.embedding_vector = []

    #ImageAnalyze 객체가 실행되면 가장 먼저 실행되는 함수
    async def execute(self):
        self.download_image()
        await self.analyze_image()
        self.choose_category()
        await self.make_keywords()
        self.make_embedding_vector()

    #Response 형태로 만들어주는 함수
    def get_response(self) -> ImageResponse:
        return ImageResponse(
            category_id=self.category_id,
            content=self.content,
            keywords=self.keywords,
            embedding_vector=self.embedding_vector
        )

    #url에서 이미지를 다운로드하는 함수
    def download_image(self):
        self.image_bytes = requests.get(self.url).content

    #이미지를 분석하는 함수
    async def analyze_image(self):
        messages = make_analyze_prompt()

        response = self.llm.chat_with_image(
            system=messages[0]['content'],
            user=messages[1]['content'],
            image_bytes=self.image_bytes
        )
        self.content = await self.translate_text(response)

        print(f'이미지 내용:\n{self.content}')

    #category를 선택하는 함수
    def choose_category(self):
        messages = make_category_prompt(self.content)

        find_category = False
        attempt_count = 0
        while attempt_count < self.MAX_CATEGORY_TRIES:
            response = self.llm.chat_with_image_json(
                system=messages[0]['content'],
                user=messages[1]['content'],
                image_bytes=self.image_bytes,
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

    #keywords를 생성하는 함수
    async def make_keywords(self):
        messages = make_keywords_content_prompt(self.content)

        response = self.llm.chat_with_image_json(
            system=messages[0]['content'],
            user=messages[1]['content'],
            image_bytes=self.image_bytes,
            schema=KeywordResponse
        )
        self.keywords = json.loads(response)['keyword']
        self.keywords = await asyncio.gather(*[self.translate_text(keyword) for keyword in self.keywords])

        print(f'키워드: {self.keywords}')

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)

    #한글로 번역하는 함수
    async def translate_text(self, text: str):
        translator = googletrans.Translator()
        result = await translator.translate(text, dest = 'ko', src = 'en')
        return result.text
