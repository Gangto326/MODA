from typing import List

from app.services.llm_client import LLMClient


class Embedding:
    def __init__(self):
        self.llm = LLMClient()

    def embed_document(self, content: str) -> List[float]:
        return self.llm.embed(content)
