package com.ssafy.enjoytrip.everywhere.map.repository;

import com.ssafy.enjoytrip.everywhere.map.dto.request.LocationSearchRequest;
import com.ssafy.enjoytrip.everywhere.ai.service.OpenAiEmbeddingClient;
import com.ssafy.enjoytrip.everywhere.map.dto.request.NearAttractionRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import io.redisearch.Document;
import io.redisearch.Query;
import io.redisearch.SearchResult;
import io.redisearch.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class AttractionRedisRepositoryImpl implements AttractionRedisRepository {

    private final Client placeSearchClient;
    private final OpenAiEmbeddingClient embeddingClient;

    public List<AttractionSimpleResponse> findAll() {
        Query query = new Query("*")
                .limit(0, 3000); // 최대 1000개까지만 조회
        SearchResult result = placeSearchClient.search(query);

        return result.docs.stream()
                .map(AttractionSimpleResponse::toResponse)
                .collect(Collectors.toList());
    }

    public AttractionSimpleResponse findByContentId(Long contentId) {
        Document doc = placeSearchClient.getDocument("attraction:" + contentId);
        if (doc == null) return null;
        return AttractionSimpleResponse.toResponse(doc);
    }

    public List<AttractionSimpleResponse> findByContentIds(List<Long> contentIds) {
        List<AttractionSimpleResponse> results = new ArrayList<>();

        for (Long id : contentIds) {
            Document doc = placeSearchClient.getDocument("attraction:" + id);
            if (doc != null) {
                results.add(AttractionSimpleResponse.toResponse(doc));
            }
        }
        return results;
    }

    @Override
    public List<AttractionSimpleResponse> searchByKeywordEmbedding(String keyword) {
        List<Float> embedding = embeddingClient.getEmbedding(keyword);
        StringBuilder vectorBuilder = new StringBuilder();

        for (Float f : embedding) {
            vectorBuilder.append(f).append(" ");
        }

        String vectorStr = vectorBuilder.toString().trim(); // 공백으로 구분된 float 값
        String queryStr =
                String.format("*=>[KNN 10 @embedding VECTOR[%s] AS score] SORTBY score ASC RETURN 3 id title score", vectorStr);

        Query query = new Query(queryStr);

        SearchResult result = placeSearchClient.search(query);

        return toResponseList(result);
    }

    public List<AttractionSimpleResponse> findByTitle(String keyword) {
        String escaped = escape(keyword.split(" ")[0]);
        System.out.println(escaped);
        Query query = new Query("@title:%" + escaped + "%");
        SearchResult result = placeSearchClient.search(query);
        return toResponseList(result);
    }

    public List<AttractionSimpleResponse> findByCategory(String category) {
        String escaped = escape(category);
        Query query = new Query("@category_name:{" + escaped + "}");
        SearchResult result = placeSearchClient.search(query);
        return toResponseList(result);
    }

    public List<AttractionSimpleResponse> findByContentType(String typeName) {
        String escaped = escape(typeName);
        Query query = new Query("@content_type_name:{" + escaped + "}");
        SearchResult result = placeSearchClient.search(query);
        return toResponseList(result);
    }

    public List<AttractionSimpleResponse> findByAreaCode(String siGunGuLike) {
        String escaped = escape(siGunGuLike);
        Query query = new Query("@si_gun_gu_name:%" + escaped + "%");
        SearchResult result = placeSearchClient.search(query);
        return toResponseList(result);
    }

    public List<AttractionSimpleResponse> findNearBy(LocationSearchRequest locationRequest) {
        String geoQuery = String.format("@location:[%.6f %.6f %.1f km]",
                locationRequest.getLongitude(),
                locationRequest.getLatitude(),
                locationRequest.getRadiuskm());

        Query query = new Query(geoQuery).limit(0, 200); // 최대 100개
        SearchResult result = placeSearchClient.search(query);

        return toResponseList(result);
    }

    @Override
    public  List<AttractionSimpleResponse> findNearByLocationAndCategory(NearAttractionRequest request) {
        // 쿼리 escape
        String escapedCategory = escape(request.getQuery());

        // GEO 쿼리 구성: 반경 내 + 카테고리 매칭
        String geoQuery = String.format("@location:[%.6f %.6f %.1f km] @category_name:{%s}",
                request.getLongitude(),
                request.getLatitude(),
                request.getRadiuskm(),
                escapedCategory
        );

        // Redisearch 질의 실행
        Query query = new Query(geoQuery).limit(0, 200); // 최대 200개 제한
        SearchResult result = placeSearchClient.search(query);

        return toResponseList(result);
    }

    private String escape(String value) {
        return value.replaceAll("([,{}\\[\\]\\(\\)\\|\"@:\\-])", "\\\\$1");
    }

    private List<AttractionSimpleResponse> toResponseList(SearchResult result) {
        return result.docs
                .stream()
                .map(AttractionSimpleResponse::toResponse)
                .collect(Collectors.toList());
    }

}
