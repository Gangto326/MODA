# # Dockerfile
# FROM python:3.12-slim
#
# # 작업 디렉토리 설정
# WORKDIR /app
#
# # Poetry 설치
# RUN pip install poetry
#
# # Poetry 가상환경 생성하지 않도록 설정
# RUN poetry config virtualenvs.create false
#
# # 프로젝트 의존성 파일 복사
# COPY pyproject.toml poetry.lock ./
#
# # 의존성 설치
# RUN poetry install --no-root
#
# # 소스코드 복사
# COPY . .
#
# # 실행 명령
# CMD ["uvicorn", "app.main:app", "--host=0.0.0.0", "--port=,8000", " --workers=4"]

FROM python:3.12-slim as builder

WORKDIR /app

RUN pip install poetry

COPY pyproject.toml poetry.lock ./

RUN poetry config virtualenvs.create false

RUN poetry install --only main

FROM python:3.12-slim

WORKDIR /app

RUN pip install uvicorn

COPY --from=builder /usr/local/lib/python3.12/site-packages /usr/local/lib/python3.12/site-packages

COPY . .

CMD ["uvicorn", "app.main:app", "--host=0.0.0.0", "--port=8000", "--workers=4"]