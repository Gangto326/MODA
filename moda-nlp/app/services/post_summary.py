import json
import time

import googletrans
import ollama

from app.constants.category import categories_name, categories
from app.constants.post_prompt import make_summary_prompt, make_keywords_content_prompt, \
    make_thumbnail_content_prompt, make_category_prompt
from app.schemas.post import PostResponse
from app.services.embedding import Embedding, vector_compare


class PostSummary:
    MAX_CATEGORY_TRIES = 10

    def __init__(self, origin_content: str):
        self.models = [
            'anpigon/qwen2.5-7b-instruct-kowiki',
            'qwen2.5',
            'hf.co/Bllossom/llama-3.2-Korean-Bllossom-3B-gguf-Q4_K_M'
        ]

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
        summary_content_idx = 0
        while True:
            self.summary_content(summary_content_idx)

            if not await self.detect_chinese(self.content):
                break

            summary_content_idx += 1
        print("카테고리 선택 시작")
        self.choose_category(0)
        print("키워드 생성 시작")
        self.make_keywords(0)
        print("썸네일 시작")
        thumbnail_content_idx = 0
        while True:
            self.make_thumbnail_content(thumbnail_content_idx)

            if not await self.detect_chinese(self.thumbnail_content):
                break

            thumbnail_content_idx += 1
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
             format = None,
             max_retries: int = 3,
             current_retry: int = 0):
        try:
            response = ollama.chat(
                model=model,
                messages=messages,
                format=format
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
                    find_category = True
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

    #origin_content를 요약하는 함수
    def summary_content(self, idxModel: int):
        try:
            # has_html_tag = any(tag in self.origin_content for tag in ['<h1>', '<h2>', '<h3>'])
            has_html_tag = False

            messages = make_summary_prompt(self.origin_content, has_html_tag)
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
                    },
                    'required': ['title', 'content']
                }
            }

            response = self.chat(model = self.models[idxModel], messages = messages, format = format)
            data = json.loads(response)

            contents = []
            for paragraph in data:
                if 'title' not in paragraph or 'content' not in paragraph:
                    return Exception(data)

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
            self.summary_content((idxModel + 1) % len(self.models))

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

    # thumbnail_content를 생성하는 함수
    def make_thumbnail_content(self, idxModel: int):
        try:
            messages = make_thumbnail_content_prompt(self.content)
            format = None

            response = self.chat(model = self.models[idxModel], messages = messages, format = format)
            self.thumbnail_content = response
            print("썸네일 생성")
        except Exception as e:
            print(f"에러: {e}")
            self.make_thumbnail_content((idxModel + 1) % len(self.models))

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