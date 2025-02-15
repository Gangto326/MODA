import json
import time

from fastapi import APIRouter, HTTPException

from app.schemas.image import ImageRequest
from app.schemas.post import PostRequest
from app.schemas.youtube import YoutubeRequest
from app.services.image_analyze import ImageAnalyze
from app.services.post_summary import PostSummary
from app.services.youtube_process import YoutubeProcess

router = APIRouter(
    prefix="/summary",
    tags=["summary"],
    responses={404: {"description": "Not found"}}
)

@router.post("/post")
async def summary_document(post_request: PostRequest):
    try:
        print("포스트 요약 시작: " + post_request)
        start_time = time.time()

        summarizer = PostSummary(post_request.content)
        await summarizer.execute()

        process_time = time.time() - start_time
        print(f"포스트 요약 완료 - {process_time:.2f}초")

        response = summarizer.get_response()
        print(json.dumps(response.model_dump(exclude={'embedding_vector'}), indent=2, ensure_ascii=False))
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/youtube")
async def process_youtube(youtube_request: YoutubeRequest):
    try:
        print("유튜브 후처리 시작: " + youtube_request)
        start_time = time.time()

        processer = YoutubeProcess(youtube_request.paragraph)
        await processer.execute()

        process_time = time.time() - start_time
        print(f"유튜브 후처리 완료 - {process_time:.2f}초")

        response = processer.get_response()
        print(json.dumps(response.model_dump(exclude={'embedding_vector'}), indent=2, ensure_ascii=False))
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/image")
async def analyze_image(image_request: ImageRequest):
    try:
        print("이미지 분석 시작: " + image_request)
        start_time = time.time()

        analyzer = ImageAnalyze(image_request.url)
        await analyzer.execute()

        process_time = time.time() - start_time
        print(f"이미지 분석 완료 - {process_time:.2f}초")

        response = analyzer.get_response()
        print(json.dumps(response.model_dump(exclude={'embedding_vector'}), indent=2, ensure_ascii=False))
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))