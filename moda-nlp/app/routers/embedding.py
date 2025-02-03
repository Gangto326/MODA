from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.services.embedding import Embedding
from typing import List

router = APIRouter(
    prefix="/embedding",
    tags=["embedding"],
    responses={404: {"description": "Not found"}}
)

class Document(BaseModel):
    content: str

embedder = Embedding()

@router.post("")
async def embed_document(document: Document):
    try:
        embedding_vector = embedder.embed_document(document.content)
        return embedding_vector.tolist()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))