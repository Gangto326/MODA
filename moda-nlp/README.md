# 프로젝트 클론 후 세팅
## UBUNTU
0. sudo apt install python3-venv
1. python3 -m venv .venv
2. source .venv/bin/activate
3. pip install poetry
4. poetry install
## WINDOWS
1. pip install poetry
2. poetry install

# 프로젝트 실행 방법
1. 루트 디렉토리에서 cmd 실행
2. 가상환경 진입
   - win: .venv\Scripts\activate.bat
   - ubu: source .venv/bin/activate
3. 프로젝트 실행
   - 개발: uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
   - 배포: uvicorn app.main:app --host 0.0.0.0 --port 8000 --workers 4
   - ubu: nohup uvicorn app.main:app --host 0.0.0.0 --port 8000 --workers 4 &