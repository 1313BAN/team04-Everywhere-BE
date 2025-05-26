import numpy as np
# Redis 연결
import re
from redis import Redis
from redis.commands.search.query import Query

r = Redis(host="localhost", port=6379)

# ✅ embedding 벡터를 float32 bytes로 변환
def prepare_embedding_vector(vec):
    return np.array(vec, dtype=np.float32).tobytes()

# ✅ 특수문자 이스케이프 및 공백 처리
def escape_and_quote(text):
    # 특수문자 이스케이프
    text = re.sub(r'([@{}[\]\"~*%\\])', r'\\\1', text)
    # 공백이 있을 경우 전체를 따옴표로 묶음
    if " " in text:
        text = f'"{text}"'
    return text

# ✅ 검색 함수
def search_attractions(query_data):
    base_query = "*"
    filters = []

    # 텍스트 필드 LIKE 검색
    for field in ["title", "si_gun_gu_name", "overview"]:
        if field in query_data:
            value = query_data[field]
            re_value = escape_and_quote(value)
            filters.append(f"@{field}:%{re_value}%")

    # 태그 필드 정확 일치
    for field in ["category_name", "content_type_name"]:
        if field in query_data:
            value = query_data[field].replace(",", "\\,")  # 태그 필드에 쉼표 이스케이프
            filters.append(f"@{field}:{{{value}}}")

    # GEO 필터
    if "location" in query_data:
        lon, lat, radius_km = map(float, query_data["location"].split())
        filters.append(f"@location:[{lon} {lat} {radius_km} km]")

    # 필터 조합
    if filters:
        base_query = " ".join(filters)

    # KNN 벡터 검색
    if "embedding" in query_data:
        embedding_vec = prepare_embedding_vector(query_data["embedding"])
        knn_clause = "=>[KNN 5 @embedding $vec_param AS score]"
        full_query = base_query + " " + knn_clause
        query = Query(full_query)\
            .return_fields("title", "score")\
            .sort_by("score")\
            .dialect(2)
        params_dict = {"vec_param": embedding_vec}
    else:
        query = Query(base_query).return_fields("title").dialect(2)
        params_dict = {}

    # Redis 검색
    results = r.ft("idx:attraction").search(query, query_params=params_dict)
    # return [doc.__dict__ for doc in results.docs]
    print(results.docs)
    return [doc.id.split(":")[1] for doc in results.docs]
    # return [{"id": doc.id, **doc.__dict__} for doc in results.docs]
