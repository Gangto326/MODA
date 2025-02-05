from transformers import pipeline

class Summary:
    def __init__(self, task: str = 'summarization', model_name: str = 'digit82/kobart-summarization'):
        self.pipe = pipeline(task=task, model=model_name)

    def summary_document(self,
                       content: str):
        return self.pipe(content)[0]['summary_text']