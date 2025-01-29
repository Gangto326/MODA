# uvicorn app.main:app --reload --host 0.0.0.0 --port 8000 --workers 4

from fastapi import FastAPI, APIRouter
from app.routers import embedding

app = FastAPI(
    title="Natural Language Processing API",
    description="API for document embedding",
    version="1.0.0"
)

# 메인 라우터를 앱에 포함
app.include_router(embedding.router, prefix="/nlp")