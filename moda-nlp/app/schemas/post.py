from pydantic import BaseModel
from typing import List

class PostRequest(BaseModel):
    content: str

class PostResponse(BaseModel):
    category_id: int
    content: str
    keywords: List[str]
    thumbnail_content: str
    embedding_vector: List[float]