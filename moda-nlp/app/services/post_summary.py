import json

import ollama

from app.constants.category import categories_name
from app.constants.post_prompt import make_summary_prompt, make_keywords_content_prompt, \
    make_thumbnail_content_prompt, make_category_prompt
from app.schemas.post import PostResponse
from app.services.embedding import Embedding


class PostSummary:
    MAX_CATEGORY_TRIES = 10
    MODEL = 'qwen2.5'

    def __init__(self, origin_content: str):
        self.origin_content = origin_content
        self.embedder = Embedding()
        self.category = ''

        self.category_id = 0
        self.content = ''
        self.keywords = []
        self.thumbnail_content = ''
        self.embedding_vector = []

    #PostSummary 객체가 실행되면 가장 먼저 실행되는 함수
    async def execute(self):
        self.choose_category()
        self.summary_content()
        self.make_keywords()
        self.make_thumbnail_content()
        self.make_embedding_vector()

    #Response 형태로 만들어주는 함수
    def get_response(self) -> PostResponse:
        return PostResponse(
            category_id=self.category_id,
            content=self.content,
            keywords=self.keywords,
            thumbnail_content=self.thumbnail_content,
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

    #category를 선택하는 함수
    def choose_category(self):
        model = self.MODEL
        messages = make_category_prompt(self.origin_content)
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
                    self.category_id = idx + 1
                    self.category = category
                    break

            if find_category:
                break

            attempt_count += 1

        if attempt_count == self.MAX_CATEGORY_TRIES:
            self.category_id = 0
            self.category = 'ALL'

        print(f'카테고리: {self.category}')

    #origin_content를 요약하는 함수
    def summary_content(self):
        model = self.MODEL
        messages = make_summary_prompt(self.category, self.origin_content)
        format = None

        response = self.chat(model = model, messages = messages, format = format)
        self.content = str(response).removeprefix("```markdown\n").removesuffix("```")

        print(f'요약본:\n{self.content}')

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
        self.keywords = json.loads(response)['keyword']
        self.keywords = [keyword for keyword in self.keywords if keyword in self.content]

        print(f'키워드: {self.keywords}')

    # thumbnail_content를 생성하는 함수
    def make_thumbnail_content(self):
        model = self.MODEL
        messages = make_thumbnail_content_prompt(self.content)
        format = None

        response = self.chat(model = model, messages = messages, format = format)
        self.thumbnail_content = response

        print(f'썸네일 요약본: {self.thumbnail_content}')

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)