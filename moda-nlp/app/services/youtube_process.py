import json
from typing import List

import ollama

from app.constants.category import categories_name, categories
from app.constants.youtube_prompt import make_category_prompt, make_keywords_content_prompt
from app.schemas.youtube import YoutubeResponse, TitleAndContent
from app.services.embedding import Embedding, vector_compare


class YoutubeProcess:
    MAX_CATEGORY_TRIES = 10

    def __init__(self, origin_paragraph: List[TitleAndContent]):
        self.models = [
            'anpigon/qwen2.5-7b-instruct-kowiki',
            'kwangsuklee/Qwen2.5-14B-Gutenberg-1e-Delta.Q5_K_M',
            'qwen2.5',
            'hf.co/Bllossom/llama-3.2-Korean-Bllossom-3B-gguf-Q4_K_M'
        ]

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
        self.process_paragraph()
        print("카테고리 선택 시작")
        self.choose_category(0)
        print("키워드 생성 시작")
        self.make_keywords(0)
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
        response = ollama.chat(
            model = model,
            messages = messages,
            format = format
        )
        return response['message']['content']

    #origin_paragraph의 데이터를 처리하는 함수
    def process_paragraph(self):
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
    def choose_category(self, idxModel: int):
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

            response = self.chat(model = self.models[idxModel], messages = messages, format = format)

            print(f" 카테고리 선택 시도 {response}")

            for idx, category in enumerate(categories_name()):
                if category.lower() in response.lower():
                    self.category_id = idx + 1
                    self.category = category
                    break

            if self.category_id == 0 and idxModel + 1 < len(self.models):
                self.choose_category(idxModel + 1)

            if self.category_id == 0:
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
            self.choose_category((idxModel + 1) % len(self.models))

    #keywords를 생성하는 함수
    def make_keywords(self, idxModel: int):
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

            response = self.chat(model = self.models[idxModel], messages = messages, format = format)
            data = json.loads(response)

            if 'keyword' not in data:
                return Exception(data)

            self.keywords = data['keyword']
            self.keywords = [keyword for keyword in self.keywords if len(keyword) <=  10 and keyword in self.content][:5]
            print("키워드 생성")
        except Exception as e:
            print(f"에러: {e}")
            self.make_keywords((idxModel + 1) % len(self.models))

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)
        print("임베딩 생성")