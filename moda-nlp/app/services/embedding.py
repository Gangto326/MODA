from sentence_transformers import SentenceTransformer, util
import numpy as np
import torch
from typing import List

class Embedding:
    def __init__(self, model_name: str = 'jhgan/ko-sroberta-multitask', cache_folder: str = './models'):
        self.model = SentenceTransformer(model_name, cache_folder=cache_folder)
        self.device = 'cuda' if torch.cuda.is_available() else 'cpu'
        self.model.to(self.device)

    def embed_document(self,
                       content: str,
                       batch_size: int = 32) -> List[float]:
        embeddings = self.model.encode(
            content,
            batch_size=batch_size,
            convert_to_numpy=True
        )

        return embeddings.tolist()

def vector_compare(vector1: List[float],
                   vector2: List[float]):
    return util.cos_sim(vector1, vector2).item()