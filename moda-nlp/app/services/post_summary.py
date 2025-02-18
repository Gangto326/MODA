import json
import random
import time

import googletrans
import ollama

from app.constants.category import categories_name
from app.constants.post_prompt import make_summary_prompt, make_keywords_content_prompt, \
    make_thumbnail_content_prompt, make_category_prompt
from app.schemas.post import PostResponse
from app.services.embedding import Embedding


class PostSummary:
    MAX_CATEGORY_TRIES = 10

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
        self.choose_category('kwangsuklee/Qwen2.5-14B-Gutenberg-1e-Delta.Q5_K_M')
        while True:
            self.summary_content('kwangsuklee/Qwen2.5-14B-Gutenberg-1e-Delta.Q5_K_M')

            if not await self.detect_chinese(self.content):
                break
        self.make_keywords('kwangsuklee/Qwen2.5-14B-Gutenberg-1e-Delta.Q5_K_M')
        while True:
            self.make_thumbnail_content('kwangsuklee/Qwen2.5-14B-Gutenberg-1e-Delta.Q5_K_M')

            if not await self.detect_chinese(self.thumbnail_content):
                break
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

    #category를 선택하는 함수
    def choose_category(self, model: str):
        try:
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
                messages = make_category_prompt(self.origin_content, exclude)
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

                exclude.append(json.loads(response)['category'])
                attempt_count += 1

            if attempt_count == self.MAX_CATEGORY_TRIES:
                self.category_id = 1
                self.category = 'All'
        except Exception as e:
            print(f"에러: {e}")
            self.choose_category(model)

    #origin_content를 요약하는 함수
    def summary_content(self, model: str):
        try:
            # has_html_tag = any(tag in self.origin_content for tag in ['<h1>', '<h2>', '<h3>'])
            has_html_tag = False

            messages = make_summary_prompt(self.category, self.origin_content, has_html_tag)
            format = {
                'type': 'array',
                'items': {
                    'type': 'object',
                    'properties': {
                        'title': {
                            'type': 'string'
                        },
                        'content': {
                            'type': 'array',
                            'items': {
                                'type': 'string'
                            }
                        }
                    }
                },
                'required': ['title', 'content']
            }

            response = self.chat(model = model, messages = messages, format = format)
            data = json.loads(response)

            print(data)

            contents = []
            for paragraph in data:
                # 제목 저장
                contents.append('# ' + paragraph['title'])

                # 각 줄 처리
                processed_lines = []
                for line in paragraph['content']:
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
            print("포스트 요약본 생성")
        except Exception as e:
            print(f"에러: {e}")
            self.summary_content(model)

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

    # thumbnail_content를 생성하는 함수
    def make_thumbnail_content(self, model: str):
        try:
            messages = make_thumbnail_content_prompt(self.content)
            format = None

            response = self.chat(model = model, messages = messages, format = format)
            self.thumbnail_content = response
            print("썸네일 생성")
        except Exception as e:
            print(f"에러: {e}")
            self.make_thumbnail_content(model)

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)
        print("임베딩 생성")

    #AI 결과에 중국어가 포함되어 있는지 확인하는 함수
    async def detect_chinese(self, content: str) -> bool:
        lines = content.split('\n')

        translator = googletrans.Translator()

        for line in lines:
            if line.strip():
                result = await translator.detect(line)
                if 'zh' in result.lang:
                    print(f'중국어가 감지되었습니다: {line}')
                    return True

        return False