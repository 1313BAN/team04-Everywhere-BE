from typing import List

from redis_openai_client import get_openai_client


def make_category_prompt(keywords: List[str]) -> str:
    keyword_str = ", ".join(keywords)

    return f"""다음 장소를 보고 카테고리를 반환하시오.  
[예시] 장소: 경복궁, 창경궁, 덕수궁 return: ['궁궐', '역사', '전통']
주의. return 이후의 딕셔너리만 반환 할것.
장소: {keyword_str} return: """


def get_best_place(keywords):
    prompt = make_category_prompt(keywords)

    response = get_openai_client().chat.completions.create(
        model="gpt-4o",
        messages=[{"role": "user", "content": prompt}],
        temperature=0.3
    )

    try:
        content = response.choices[0].message.content
        print(content)
        return content
    except Exception as e:
        print("❌ 파싱 실패:", e)
        print("내용:", content)
        return {"title": None}


if __name__ == "__main__":
    # ✅ 테스트 실행
    keywords = ["일영유원지"]
    best_place = get_best_place(keywords)
    # print("✅ 추천 장소:", best_place["title"])
