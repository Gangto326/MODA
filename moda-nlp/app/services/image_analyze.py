import base64
import json

import googletrans
import ollama
import requests

from app.constants.category import categories_name
from app.constants.image_prompt import make_analyze_prompt, make_category_prompt, make_keywords_content_prompt
from app.schemas.image import ImageResponse
from app.services.embedding import Embedding


class ImageAnalyze:
    MAX_CATEGORY_TRIES = 10
    MODEL = 'llama3.2-vision'

    def __init__(self, url: str):
        self.url = url
        self.base64_image = []
        self.embedder = Embedding()
        self.category = ''

        self.category_id = 0
        self.content = ''
        self.keywords = []
        self.embedding_vector = []

    #ImageAnalyze 객체가 실행되면 가장 먼저 실행되는 함수
    async def execute(self):
        await self.encode_base64()
        await self.choose_category()
        await self.analyze_image()
        await self.make_keywords()
        await self.make_embedding_vector()

    #Response 형태로 만들어주는 함수
    async def get_response(self) -> ImageResponse:
        return ImageResponse(
            category_id=self.category_id,
            content=self.content,
            keywords=self.keywords,
            embedding_vector=self.embedding_vector
        )

    #ollama 채팅을 진행하는 함수
    async def chat(self,
             messages,
             model: str = MODEL,
             format = None):
        response = ollama.chat(
            model = model,
            messages = messages,
            format = format
        )
        return response['message']['content']

    #url을 base64로 인코딩하는 함수
    async def encode_base64(self):
        self.base64_image = [base64.b64encode(requests.get(self.url).content).decode()]

    #category를 선택하는 함수
    async def choose_category(self):
        model = self.MODEL
        messages = make_category_prompt(self.base64_image)
        format = {
            'type': 'object',
            'properties': {
                'category': {
                    'type': 'string'
                }
            },
            'required': ['category']
        }

        find_category = False
        attempt_count = 0
        while attempt_count < self.MAX_CATEGORY_TRIES:
            response = await self.chat(model = model, messages = messages, format = format)

            for idx, category in enumerate(categories_name()):
                if category.lower() in response.lower():
                    find_category = True
                    self.category_id = idx
                    self.category = category
                    break

            if find_category:
                break

            attempt_count += 1

        if attempt_count == self.MAX_CATEGORY_TRIES:
            self.category_id = 0
            self.category = 'ALL'

        print(f'카테고리: {self.category}')

    #base64_image를 통해 이미지를 분석하는 함수
    async def analyze_image(self):
        model = self.MODEL
        messages = make_analyze_prompt(self.base64_image)
        format = None

        response = await self.chat(model = model, messages = messages, format = format)
        self.content = await self.translate_text(response)

        print(f'이미지 내용:\n{self.content}')

    #keywords를 생성하는 함수
    async def make_keywords(self):
        model = self.MODEL
        messages = make_keywords_content_prompt(self.base64_image)
        format = {
            'type': 'object',
            'properties': {
                'keyword': {
                    'type': 'array',
                    'items': {
                        'type': 'string'
                    }
                }
            },
            'required': ['keyword']
        }

        response = await self.chat(model = model, messages = messages, format = format)
        self.keywords = json.loads(response)['keyword']
        #TODO: 키워드 번역

        print(f'키워드: {self.keywords}')

    #embeeding_vector를 생성하는 함수
    async def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)

    #한글로 번역하는 함수
    async def translate_text(self, text: str):
        translator = googletrans.Translator()
        return await translator.translate(text, dest = 'ko', src = 'en')