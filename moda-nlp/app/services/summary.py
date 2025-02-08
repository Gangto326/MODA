import ollama
from typing import List, Dict
from app.constants.category import categories, categories_name
from app.constants.prompt import category_prompt
from app.services.embedding import Embedding
from app.schemas.post import PostResponse

def system_prompt(prompt: str):
    return {
        'role': 'system',
        'content': f'{prompt}'
    }

def user_prompt(prompt: str):
    return {
        'role': 'user',
        'content': f'{prompt}'
    }

def assistant_prompt(prompt: str):
    return {
        'role': 'assistant',
        'content': f'{prompt}'
    }

class Summary:
    MAX_CATEGORY_TRIES = 10

    def __init__(self, origin_content: str):
        self.messages = []

        self.origin_content = origin_content
        self.embedder = Embedding()
        self.category = ''

        self.category_id = 0
        self.content = ''
        self.keywords = []
        self.thumbnail_content = ''
        self.embedding_vector = []

        self.execute()

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
             model: str = 'hf.co/Bllossom/llama-3.2-Korean-Bllossom-3B-gguf-Q4_K_M',
             format = None):
        response = ollama.chat(
            model = model,
            messages = messages,
            format = format
        )
        return response['message']['content']

    #Summary 객체가 실행되면 가장 먼저 실행되는 함수
    def execute(self):
        print("embedding")
        self.make_embedding_vector()
        print("choose")
        self.choose_category()
        # TODO: prompt
        # TODO: content
        # TODO: keywords
        # TODO: thumbnail_content

    #embeeding_vector를 생성하는 함수
    def make_embedding_vector(self):
        self.embedding_vector = self.embedder.embed_document(self.content)

    #category를 선택하는 함수
    def choose_category(self):
        messages = category_prompt
        messages[1]['content'] += self.origin_content

        find_category = False
        attempt_count = 0
        while attempt_count < self.MAX_CATEGORY_TRIES:
            selected = self.chat(messages)

            print(attempt_count, selected)

            for idx, category in enumerate(categories):
                if category == selected:
                    find_category = True
                    self.category_id = idx
                    self.category = category

            if find_category:
                break

            attempt_count += 1

        if attempt_count == self.MAX_CATEGORY_TRIES:
            self.category_id = 0
            self.category = 'ALL'