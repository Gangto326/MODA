from typing import List

from pydantic import BaseModel


class TitleAndContent(BaseModel):
    title: str
    content: str

class YoutubeRequest(BaseModel):
    paragraph: List[TitleAndContent]

class YoutubeResponse(BaseModel):
    category_id: int
    content: str
    keywords: List[str]
    embedding_vector: List[float]