import time
import logging

from fastapi import APIRouter, HTTPException

from app.schemas.crawl import CrawlRequest, CrawlResponse
from app.services.crawl_and_analyze import CrawlAndAnalyze

logger = logging.getLogger(__name__)

router = APIRouter()


@router.post("", response_model=CrawlResponse)
async def crawl_and_analyze(request: CrawlRequest):
    start = time.time()
    logger.info("[/nlp/crawl] URL: %s", request.url)

    try:
        analyzer = CrawlAndAnalyze(request.url)
        await analyzer.execute()
        response = analyzer.get_response()

        elapsed = time.time() - start
        logger.info("[/nlp/crawl] Completed in %.2fs — title='%s', domain=%s",
                     elapsed, response.title, response.domain_type)
        return response

    except Exception as e:
        elapsed = time.time() - start
        logger.error("[/nlp/crawl] Failed after %.2fs — %s: %s", elapsed, type(e).__name__, str(e))
        raise HTTPException(status_code=500, detail=f"크롤링 및 분석 실패: {str(e)}")
