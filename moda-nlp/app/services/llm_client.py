import base64
import hashlib
import json
import os
import struct

from dotenv import load_dotenv
from openai import OpenAI
from pydantic import BaseModel
from typing import List

load_dotenv()

OPENGATEWAY_API_KEY = os.getenv('OPENGATEWAY_API_KEY')
OPENGATEWAY_BASE_URL = 'https://apis.opengateway.ai/v1'


# 3개 서비스에서 공통 사용하는 Pydantic 스키마
class CategoryResponse(BaseModel):
    category: str


class KeywordResponse(BaseModel):
    keyword: List[str]


class KeywordAndThumbnailResponse(BaseModel):
    keyword: List[str]
    thumbnail_content: str


def _strip_json(text: str) -> str:
    """마크다운 코드 블록(```json ... ```)을 제거하고 순수 JSON 문자열만 반환"""
    t = text.strip()
    if t.startswith("```"):
        first_newline = t.find("\n")
        if first_newline != -1:
            t = t[first_newline + 1:]
        t = t.rstrip("`").strip()
    return t


class LLMClient:
    MODEL = 'vertex-ai/gemini-2.0-flash'

    def __init__(self):
        self.client = OpenAI(
            api_key=OPENGATEWAY_API_KEY,
            base_url=OPENGATEWAY_BASE_URL,
            timeout=30.0,
        )

    def chat(self, system: str, user: str) -> str:
        response = self.client.chat.completions.create(
            model=self.MODEL,
            messages=[
                {"role": "system", "content": system},
                {"role": "user", "content": user},
            ],
        )
        return response.choices[0].message.content

    def chat_json(self, system: str, user: str, schema) -> str:
        schema_json = json.dumps(schema.model_json_schema(), ensure_ascii=False)
        system_with_schema = f"{system}\n\nRespond ONLY with valid JSON matching this schema:\n{schema_json}"

        response = self.client.chat.completions.create(
            model=self.MODEL,
            messages=[
                {"role": "system", "content": system_with_schema},
                {"role": "user", "content": user},
            ],
        )
        return _strip_json(response.choices[0].message.content)

    def chat_with_image(self, system: str, user: str, image_bytes: bytes, mime_type: str = 'image/jpeg') -> str:
        b64_image = base64.b64encode(image_bytes).decode('utf-8')
        response = self.client.chat.completions.create(
            model=self.MODEL,
            messages=[
                {"role": "system", "content": system},
                {"role": "user", "content": [
                    {"type": "text", "text": user},
                    {"type": "image_url", "image_url": {
                        "url": f"data:{mime_type};base64,{b64_image}"
                    }},
                ]},
            ],
        )
        return response.choices[0].message.content

    def chat_with_image_json(self, system: str, user: str, image_bytes: bytes, schema, mime_type: str = 'image/jpeg') -> str:
        b64_image = base64.b64encode(image_bytes).decode('utf-8')
        schema_json = json.dumps(schema.model_json_schema(), ensure_ascii=False)
        system_with_schema = f"{system}\n\nRespond ONLY with valid JSON matching this schema:\n{schema_json}"

        response = self.client.chat.completions.create(
            model=self.MODEL,
            messages=[
                {"role": "system", "content": system_with_schema},
                {"role": "user", "content": [
                    {"type": "text", "text": user},
                    {"type": "image_url", "image_url": {
                        "url": f"data:{mime_type};base64,{b64_image}"
                    }},
                ]},
            ],
        )
        return _strip_json(response.choices[0].message.content)

    def embed(self, content: str) -> List[float]:
        # 부하 테스트용: 모델 호출 1회 (실제 부하 시뮬레이션) + 더미 768차원 벡터 반환
        self.client.chat.completions.create(
            model=self.MODEL,
            messages=[
                {"role": "user", "content": f"Summarize in one word: {content[:200]}"},
            ],
            max_tokens=10,
        )
        # content 해시 기반 결정적 더미 벡터 생성 (768차원)
        digest = hashlib.sha256(content.encode()).digest()
        seed = struct.unpack('<I', digest[:4])[0]
        import random
        rng = random.Random(seed)
        vector = [rng.gauss(0, 0.1) for _ in range(768)]
        norm = sum(v * v for v in vector) ** 0.5
        return [v / norm for v in vector]
