from pydantic import BaseModel
from typing import List

class ImageRequest(BaseModel):
    url: str

class ImageResponse(BaseModel):
    category_id: int
    content: str
    keywords: List[str]
    embedding_vector: List[float]