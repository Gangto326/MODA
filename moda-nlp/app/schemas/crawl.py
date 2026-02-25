from pydantic import BaseModel
from typing import List, Optional


class CrawlRequest(BaseModel):
    url: str


class CrawlResponse(BaseModel):
    category_id: int
    content: str
    keywords: List[str]
    thumbnail_content: str
    embedding_vector: List[float]
    title: str
    thumbnail_url: Optional[str]
    images: List[str]
    domain_type: str
