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

# TODO: (h1~h3 태그가 있을 가능성 있음)
@router.post("/post")
async def summary_document(post_request: PostRequest):
    try:
        start_time = time.time()

        summarizer = PostSummary(post_request.content)

        process_time = time.time() - start_time
        print(f"포스트 요약 완료 - {process_time:.2f}초")
        return summarizer.get_response()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/youtube")
async def process_youtube(youtube_request: YoutubeRequest):
    try:
        start_time = time.time()

        # TODO: 데이터 전처리
        # TODO: 키워드 뽑기, 임베딩벡터 만들기
        processer = YoutubeProcess(youtube_request.content)

        process_time = time.time() - start_time
        print(f"유튜브 후처리 완료 - {process_time:.2f}초")
        # return summarizer.get_response()
        return processer.get_response()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/image")
async def analyze_image(image_request: ImageRequest):
    try:
        start_time = time.time()

        analyzer = ImageAnalyze(image_request.url)

        process_time = time.time() - start_time
        print(f"이미지 분석 완료 - {process_time:.2f}초")
        return analyzer.get_response()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))