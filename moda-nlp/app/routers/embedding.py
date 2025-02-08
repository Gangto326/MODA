from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.services.embedding import Embedding
import time

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
        start_time = time.time()
        embedding_vector = embedder.embed_document(document.content)
        process_time = time.time() - start_time
        print(f"문서 임베딩 완료 - {process_time:.2f}초")
        return embedding_vector
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))