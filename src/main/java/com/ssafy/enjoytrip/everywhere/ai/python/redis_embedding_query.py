from redis_openai_client import get_openai_client


def get_embedding(text: str) -> list[float]:
    if not text or not isinstance(text, str):
        raise ValueError("Embedding input must be a non-empty string")

    response = get_openai_client().embeddings.create(
        model="text-embedding-3-large",
        input=[text]
    )
    return response.data[0].embedding