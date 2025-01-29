from fastapi import FastAPI, APIRouter
import uvicorn

app = FastAPI(
    title="Natural Language Processing API",
    description="API for document embedding",
    version="1.0.0"
)

@app.get("/")
def test():
    print("hello world")
    return "hello world"

if __name__ == "__main__":
    uvicorn.run(app,
                host="0.0.0.0",
                port=8000)