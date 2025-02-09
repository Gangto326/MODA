from pydantic import BaseModel
from typing import List

class Content(BaseModel):
    title: str
    content: str

class YoutubeRequest(BaseModel):
    paragraph: List[Content]

class YoutubeResponse(BaseModel):
    category_id: int
    content: str
    keywords: List[str]
    embedding_vector: List[float]