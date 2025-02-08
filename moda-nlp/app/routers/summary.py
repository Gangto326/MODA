from fastapi import APIRouter, HTTPException, Request
from app.schemas.post import PostRequest
from app.services.summary import Summary
import time

router = APIRouter(
    prefix="/summary",
    tags=["summary"],
    responses={404: {"description": "Not found"}}
)

@router.post("/post")
async def summary_document(postRequest: PostRequest):
    try:
        start_time = time.time()

        summarizer = Summary(postRequest.content)

        process_time = time.time() - start_time
        print(f"포스트 요약 완료 - {process_time:.2f}초")
        return summarizer.get_response()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))