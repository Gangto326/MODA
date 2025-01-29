from sentence_transformers import SentenceTransformer, util
import numpy as np
import torch

class Embedding:
    def __init__(self, model_name: str = 'jhgan/ko-sroberta-multitask'):
        self.model = SentenceTransformer(model_name)
        self.device = 'cuda' if torch.cuda.is_available() else 'cpu'
        self.model.to(self.device)

    def embed_document(self,
                       document: str,
                       batch_size: int = 32) -> np.ndarray:
        embeddings = self.model.encode(
            document,
            batch_size=batch_size,
            convert_to_numpy=True
        )

        return embeddings

    def compare(self,
                document1: str,
                document2: str) -> float:
        embedding_vector1 = self.embed_document(document1)
        embedding_vector2 = self.embed_document(document2)

        return util.cos_sim(embedding_vector1, embedding_vector2).item()