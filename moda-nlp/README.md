# 프로젝트 클론 후 세팅
1. pip install poetry

2. poetry install

# 프로젝트 실행 방법

1. 루트 디렉토리에서 cmd 실행

2. 가상환경 진입
   - `.venv\Scripts\activate.bat`

3. 프로젝트 실행
   - 개발: `uvicorn app.main:app --reload --host 0.0.0.0 --port 8000`
   - 배포: `uvicorn app.main:app --host 0.0.0.0 --port 8000 --workers 4`