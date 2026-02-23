import json
from typing import List

from app.constants.category import categories_name
from app.constants.youtube_prompt import make_category_prompt, make_keywords_content_prompt, make_process_prompt
from app.schemas.youtube import YoutubeResponse, TitleAndContent
from app.services.embedding import Embedding
from app.services.llm_client import LLMClient, CategoryResponse, KeywordResponse


class YoutubeProcess:
    MAX_CATEGORY_TRIES = 10

    def __init__(self, origin_paragraph: List[TitleAndContent]):
        self.origin_content = json.dumps([p.model_dump() for p in origin_paragraph], ensure_ascii=False)
        self.llm = LLMClient()
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

    #origin_paragraph의 데이터를 처리하는 함수
    def process_paragraph(self):
        messages = make_process_prompt(self.origin_content)

        response = self.llm.chat(
            system=messages[0]['content'],
            user=messages[1]['content']
        )
        self.content = str(response).removeprefix("```markdown\n").removesuffix("```")

        print(f'처리된 내용:\n{self.content}')

    #category를 선택하는 함수
    def choose_category(self):
        messages = make_category_prompt(self.content)

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

    #keywords를 생성하는 함수
    def make_keywords(self):
        messages = make_keywords_content_prompt(self.content)

        response = self.llm.chat_json(
            system=messages[0]['content'],
            user=messages[1]['content'],
            schema=KeywordResponse
        )
        self.keywords = json.loads(response)['keyword']
        self.keywords = [keyword for keyword in self.keywords if keyword in self.content]

        print(f'키워드: {self.keywords}')

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)
