from typing import List

from fastapi import FastAPI
from pydantic import BaseModel

from redis_attraction_search import search_attractions
from redis_create_query import make_category_prompt
from redis_embedding_query import get_embedding

# uvicorn app:app --host 0.0.0.0 --port 7070 --workers 2

app = FastAPI()

class KeywordQuery(BaseModel):
    keywords: List[str]

class LocationQuery(BaseModel):
    longitude: float
    latitude: float
    radius_km: float

# 검색 키워드 기반 추천
@app.post("/api/ai/keywords")
def keywords_based_recommendation(data: KeywordQuery):
    content_ids = set()

    for keyword in data.keywords:
        query_data = {"title": keyword}
        results = search_attractions(query_data)
        content_ids.update(results)

    return list(content_ids)

# 찜 장소 기반 추천
@app.post("/api/ai/hotplaces")
def favorites_based_recommendation(data: KeywordQuery):
    content_ids = set()
    # 쿼리 생성
    prompt = make_category_prompt(data.keywords)
    query = get_embedding(prompt)
    query_data = {"embedding": query}
    # 검색
    results = search_attractions(query_data)
    content_ids.update(results)

    return list(content_ids)

@app.post("/api/ai/location")
def get_recommendations(data: LocationQuery):
    # 쿼리 생성
    query_data = {"location": f"{data.longitude} {data.latitude} {data.radius_km}"}
    # 검색
    results = search_attractions(query_data)
    return results
