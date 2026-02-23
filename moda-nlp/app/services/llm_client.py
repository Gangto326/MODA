import os

from dotenv import load_dotenv
from google import genai
from google.genai import types
from pydantic import BaseModel
from typing import List

load_dotenv()

GEMINI_API_KEY = os.getenv('GEMINI_API_KEY')


# 3개 서비스에서 공통 사용하는 Pydantic 스키마
class CategoryResponse(BaseModel):
    category: str


class KeywordResponse(BaseModel):
    keyword: List[str]


class LLMClient:
    MODEL = 'gemini-2.0-flash'
    EMBEDDING_MODEL = 'gemini-embedding-001'

    def __init__(self):
        self.client = genai.Client(api_key=GEMINI_API_KEY)

    def chat(self, system: str, user: str) -> str:
        response = self.client.models.generate_content(
            model=self.MODEL,
            contents=user,
            config=types.GenerateContentConfig(
                system_instruction=system,
            )
        )
        return response.text

    def chat_json(self, system: str, user: str, schema) -> str:
        response = self.client.models.generate_content(
            model=self.MODEL,
            contents=user,
            config=types.GenerateContentConfig(
                system_instruction=system,
                response_mime_type='application/json',
                response_schema=schema,
            )
        )
        return response.text

    def chat_with_image(self, system: str, user: str, image_bytes: bytes, mime_type: str = 'image/jpeg') -> str:
        response = self.client.models.generate_content(
            model=self.MODEL,
            contents=[
                types.Part.from_text(user),
                types.Part.from_bytes(data=image_bytes, mime_type=mime_type),
            ],
            config=types.GenerateContentConfig(
                system_instruction=system,
            )
        )
        return response.text

    def chat_with_image_json(self, system: str, user: str, image_bytes: bytes, schema, mime_type: str = 'image/jpeg') -> str:
        response = self.client.models.generate_content(
            model=self.MODEL,
            contents=[
                types.Part.from_text(user),
                types.Part.from_bytes(data=image_bytes, mime_type=mime_type),
            ],
            config=types.GenerateContentConfig(
                system_instruction=system,
                response_mime_type='application/json',
                response_schema=schema,
            )
        )
        return response.text

    def embed(self, content: str) -> List[float]:
        result = self.client.models.embed_content(
            model=self.EMBEDDING_MODEL,
            contents=content,
            config=types.EmbedContentConfig(
                output_dimensionality=768,
            )
        )
        return result.embeddings[0].values
