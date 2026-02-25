from fastapi import FastAPI, APIRouter
from prometheus_fastapi_instrumentator import Instrumentator
from app.routers import embedding
from app.routers import summary

app = FastAPI(
    title="Natural Language Processing API",
    description="API for document embedding",
    version="1.0.0"
)

# Prometheus metrics
Instrumentator().instrument(app).expose(app)

# 메인 라우터를 앱에 포함
app.include_router(embedding.router, prefix="/nlp")
app.include_router(summary.router, prefix="/nlp")