import json
from typing import List

import ollama

from app.constants.category import categories_name
from app.constants.youtube_prompt import make_category_prompt, make_keywords_content_prompt, make_process_prompt
from app.schemas.youtube import YoutubeResponse, TitleAndContent
from app.services.embedding import Embedding


class YoutubeProcess:
    MAX_CATEGORY_TRIES = 10
    MODEL = 'hf.co/heegyu/EEVE-Korean-Instruct-10.8B-v1.0-GGUF:Q4_K_M'

    def __init__(self, origin_paragraph: List[TitleAndContent]):
        self.origin_content = json.dumps([p.model_dump() for p in origin_paragraph], ensure_ascii=False)
        self.embedder = Embedding()
        self.category = ''

        self.category_id = 0
        self.content = ''
        self.keywords = []
        self.embedding_vector = []

    #YoutubeProcess 객체가 실행되면 가장 먼저 실행되는 함수
    async def execute(self):
        self.process_paragraph()
        self.choose_category()
        self.make_keywords()
        self.make_embedding_vector()

    #Response 형태로 만들어주는 함수
    def get_response(self) -> YoutubeResponse:
        return YoutubeResponse(
            category_id=self.category_id,
            content=self.content,
            keywords=self.keywords,
            embedding_vector=self.embedding_vector
        )

    #ollama 채팅을 진행하는 함수
    def chat(self,
             messages,
             model: str = MODEL,
             format = None):
        response = ollama.chat(
            model = model,
            messages = messages,
            format = format
        )
        return response['message']['content']

    #origin_paragraph의 데이터를 처리하는 함수
    def process_paragraph(self):
        model = self.MODEL
        messages = make_process_prompt(self.origin_content)
        format = None

        response = self.chat(model = model, messages = messages, format = format)
        self.content = str(response).removeprefix("```markdown\n").removesuffix("```")

    #category를 선택하는 함수
    def choose_category(self):
        model = self.MODEL
        messages = make_category_prompt(self.content)
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
            response = self.chat(model = model, messages = messages, format = format)

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

    #keywords를 생성하는 함수
    def make_keywords(self):
        model = self.MODEL
        messages = make_keywords_content_prompt(self.content)
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
        self.keywords = json.loads(response)['keyword'][:5]
        self.keywords = [keyword for keyword in self.keywords if keyword in self.content]

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)