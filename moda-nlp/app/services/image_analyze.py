import base64
import json
import time
from typing import List

import googletrans
import ollama
import requests

from app.constants.category import categories_name, categories
from app.constants.image_prompt import make_analyze_prompt, make_category_prompt, make_keywords_content_prompt
from app.schemas.image import ImageResponse
from app.services.embedding import Embedding, vector_compare


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
        self.encode_base64()
        print("이미지 분석 시작")
        await self.analyze_image()
        print("카테고리 선택 시작")
        self.choose_category()
        print("키워드 생성 시작")
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

    #ollama 채팅을 진행하는 함수
    def chat(self,
             messages,
             model: str = MODEL,
             format = None,
             max_retries: int = 3,
             current_retry: int = 0):
        try:
            response = ollama.chat(
                model = model,
                messages = messages,
                format = format
            )
            return response['message']['content']
        except Exception as e:
            print(f"올라마 에러 발생 (시도 {current_retry + 1}/{max_retries}): {e}")

            # 최대 재시도 횟수를 초과하지 않았다면 재귀적으로 다시 시도
            if current_retry < max_retries:
                print(f"3초 후 재시도합니다...")
                time.sleep(3)  # 잠시 대기 후 재시도
                return self.chat(
                    messages=messages,
                    model=model,
                    format=format,
                    max_retries=max_retries,
                    current_retry=current_retry + 1
                )
            else:
                print("최대 재시도 횟수를 초과했습니다.")
                raise


    #url을 base64로 인코딩하는 함수
    def encode_base64(self):
        self.base64_image = [base64.b64encode(requests.get(self.url).content).decode()]

    #base64_image를 통해 이미지를 분석하는 함수
    async def analyze_image(self):
        try:
            model = self.MODEL
            messages = make_analyze_prompt(self.base64_image)
            format = None

            response =  self.chat(model = model, messages = messages, format = format)
            self.content = await self.translate_text(response)
            print("이미지 분석")
        except Exception as e:
            print(f"에러: {e}")
            await self.analyze_image()

    #category를 선택하는 함수
    def choose_category(self):
        try:
            model = self.MODEL
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
            exclude = []
            while attempt_count < self.MAX_CATEGORY_TRIES:
                messages = make_category_prompt(self.content, self.base64_image, exclude)
                response = self.chat(model = model, messages = messages, format = format)

                print(f" 카테고리 선택 시도 {attempt_count} - {response}")

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
                self.category = 'ALL'

                embedding = self.embedder.embed_document(self.content)
                similarity = 0

                for idx in range(1, len(categories)):
                    compare_result = vector_compare(embedding, categories[idx][1])
                    if compare_result > similarity:
                        similarity = compare_result
                        self.category_id = idx + 1
                        self.category = categories[idx][0]

                print(f"임베딩 카테고리 {self.category_id} {self.category}")
        except Exception as e:
            print(f"에러: {e}")
            self.choose_category()

    #keywords를 생성하는 함수
    async def make_keywords(self):
        try:
            model = self.MODEL
            messages = make_keywords_content_prompt(self.content, self.base64_image)
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

            response = self.chat(model = model, messages = messages, format = format)
            data = json.loads(response)

            if 'keyword' not in data:
                return Exception(data)

            response = data['keyword']
            response = await self.translate_list(response)
            self.keywords = [keyword for keyword in response if len(keyword) <=  10]
            print("키워드 생성")
        except Exception as e:
            print(f"에러: {e}")

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)
        print("임베딩 생성")

    #한글로 번역하는 함수
    async def translate_text(self, text: str):
        translator = googletrans.Translator()
        result = await translator.translate(text, dest = 'ko', src = 'en')
        return result.text

    async def translate_list(self, list: List[str]):
        translator = googletrans.Translator()

        result = await translator.translate(list, dest = 'ko', src = 'en')

        return [word.text for word in result]