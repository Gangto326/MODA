from sentence_transformers import SentenceTransformer, util
import numpy as np
import torch
from typing import List, Union

"""
사용한 모델
    768 차원: https://huggingface.co/jhgan/ko-sroberta-multitask
    384 차원: https://huggingface.co/sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2
    768 차원: https://huggingface.co/snunlp/KR-SBERT-V40K-klueNLI-augSTS
    512 차원: https://huggingface.co/sentence-transformers/distiluse-base-multilingual-cased-v1
    768 차원: https://huggingface.co/jhgan/ko-sbert-sts
    1024 차원: https://huggingface.co/sentence-transformers/static-similarity-mrl-multilingual-v1
    
    실패: https://huggingface.co/skt/kobert-base-v1
"""

class Embedding:
    def __init__(self, model_name: str = 'jhgan/ko-sroberta-multitask'):
        self.model = SentenceTransformer(model_name)
        self.device = 'cuda' if torch.cuda.is_available() else 'cpu'
        self.model.to(self.device)

    def embed_documents(self,
                        documents: Union[str, List[str]],
                        batch_size: int = 32) -> np.ndarray:
        # 단일 문서인 경우 리스트로 변환
        if isinstance(documents, str):
            documents = [documents]

        # 임베딩 생성
        embeddings = self.model.encode(
            documents,
            batch_size=batch_size,
            convert_to_numpy=True
        )

        return embeddings

# 사용 예시
if __name__ == "__main__":
    # 사용 모델들
    models = [
        'jhgan/ko-sroberta-multitask',
        'sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2',
        'snunlp/KR-SBERT-V40K-klueNLI-augSTS',
        'sentence-transformers/distiluse-base-multilingual-cased-v1',
        'jhgan/ko-sbert-sts',
        'sentence-transformers/static-similarity-mrl-multilingual-v1',
    ]

    for model in models:
        embedder = Embedding(model)

        # 예시 문서들
        source = "잠이 옵니다"
        compare = [
            "잠이 안 옵니다",
            "졸음이 옵니다",
            "기차가 옵니다",
            "내 이름은 잠입니다"
        ]

        # source = "날씨가 좋습니다"
        # compare = [
        #     "날씨가 안 좋습니다",  # 부정문
        #     "날씨가 맑습니다",  # 유사 의미
        #     "날씨가 화창합니다",  # 유사 의미
        #     "기분이 좋습니다",  # 유사 구조, 다른 의미
        #     "하늘이 맑습니다",  # 연관 의미
        #     "비가 옵니다",  # 반대 상황
        #     "날씨는 좋다고 합니다",  # 유사 의미, 다른 구조
        #     "오늘은 날씨입니다",  # 무의미한 문장
        #     "버스가 옵니다",  # 완전히 다른 의미
        #     "내일 날씨 어떠세요"  # 관련 주제, 다른 문장 구조
        # ]

        # source = "나는 어제 친구와 영화를 봤다."
        # compare = [
        #     "어제 여자친구랑 영화관에 갔다.",
        #     "나는 어제 영화관에서 여자친구와 대판 싸웠다.",
        #     "내일 친구랑 게임을 할거다."
        # ]

        # 문서 임베딩
        source_embedding = embedder.embed_documents(source)
        compare_embeddings = embedder.embed_documents(compare)

        print(f"\n=차원: {source_embedding.shape[1]}, 모델: {model}")

        # 문서 간 유사도 계산
        similarity_matrix = util.cos_sim(source_embedding, compare_embeddings)
        similarity_matrix = list(zip(similarity_matrix[0], compare))
        similarity_matrix = sorted(similarity_matrix, key=lambda x: x[0], reverse=True)

        # 문서 간 유사도 결과
        print(f"기준 문서: {source}")
        for idx, similarity in enumerate(similarity_matrix, start=1):
            print(f"{idx}등. 유사도={similarity[0]:.2f}, {similarity[1]}")

            if idx == 5:
                break