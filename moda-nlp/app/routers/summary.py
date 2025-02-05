from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.services.summary import Summary
from typing import List
import time

router = APIRouter(
    prefix="/summary",
    tags=["summary"],
    responses={404: {"description": "Not found"}}
)

class Document(BaseModel):
    contents: List[str]

summarizer = Summary()

@router.post("")
async def embed_document(document: Document):
    try:
        start_time = time.time()

        summary = List

        for content in document.contents:
            summary.append(summarizer.summary_document(content))

        process_time = time.time() - start_time
        print(f"문서 요약 완료 - {process_time:.2f}초")
        return summary
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))