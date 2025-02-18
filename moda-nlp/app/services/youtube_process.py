import json
import random
from typing import List

import ollama

from app.constants.category import categories_name, categories
from app.constants.youtube_prompt import make_category_prompt, make_keywords_content_prompt
from app.schemas.youtube import YoutubeResponse, TitleAndContent
from app.services.embedding import Embedding, vector_compare


class YoutubeProcess:
    MAX_CATEGORY_TRIES = 10

    def __init__(self, origin_paragraph: List[TitleAndContent]):
        # self.origin_content = json.dumps([p.model_dump() for p in origin_paragraph], ensure_ascii=False)
        self.origin_paragraph = origin_paragraph
        self.embedder = Embedding()
        self.category = ''

        self.category_id = 0
        self.content = ''
        self.keywords = []
        self.embedding_vector = []

    #YoutubeProcess 객체가 실행되면 가장 먼저 실행되는 함수
    async def execute(self):
        self.process_paragraph('anpigon/qwen2.5-7b-instruct-kowiki')
        self.choose_category('anpigon/qwen2.5-7b-instruct-kowiki'),
        self.make_keywords('anpigon/qwen2.5-7b-instruct-kowiki'),
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
             model: str,
             format = None):
        current_seed = int(time.time() * 1000) + random.randint(1, 1000000)

        print(f"올라마 챗: {messages}")

        response = ollama.chat(
            model = model,
            messages = messages,
            format = format,
            options = {
                'seed': current_seed,
                'temperature': random.uniform(0.7, 0.9),  # 랜덤 temperature 값
                'top_p': random.uniform(0.8, 0.95)       # 랜덤 top_p 값
            }
        )
        return response['message']['content']

    #origin_paragraph의 데이터를 처리하는 함수
    def process_paragraph(self, model: str):
        contents = []

        for paragraph in self.origin_paragraph:
            # 제목 저장
            contents.append('# ' + paragraph.title)

            # 각 줄을 분리
            lines = paragraph.content.strip().split('\n')

            # 각 줄 처리
            processed_lines = []
            for line in lines:
                # <<숫자>> 패턴 제거
                # 정규식을 사용하지 않고 기본 문자열 처리로 구현
                if '<<' in line and '>>' in line:
                    line = line[:line.find('<<')]

                # 앞에 '- ' 추가하고 공백 제거
                processed_line = '- ' + line.strip()
                processed_lines.append(processed_line)

            # 결과 합치기
            contents.append('\n'.join(processed_lines))

        self.content = '\n'.join(contents)

    #category를 선택하는 함수
    def choose_category(self, model: str):
        try:
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

                for idx in range(len(categories)):
                    compare_result = vector_compare(embedding, categories[idx + 1][1])
                    if compare_result > similarity:
                        similarity = compare_result
                        category_id = idx + 1
                        category = categories[idx + 1][0]

                print(f"임베딩 카테고리 {self.category_id} {self.category}")
        except Exception as e:
            print(f"에러: {e}")
            self.choose_category(model)

    #keywords를 생성하는 함수
    def make_keywords(self, model: str):
        try:
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
            self.keywords = [keyword for keyword in self.keywords if len(keyword) <=  10 and keyword in self.content]
            print("키워드 생성")
        except Exception as e:
            print(f"에러: {e}")
            self.make_keywords(model)

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)
        print("임베딩 생성")