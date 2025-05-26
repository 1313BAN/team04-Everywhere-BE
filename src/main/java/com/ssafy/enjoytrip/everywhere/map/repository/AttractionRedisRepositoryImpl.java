package com.ssafy.enjoytrip.everywhere.map.repository;

import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationRequest;
import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationSearchRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import io.redisearch.Document;
import io.redisearch.Query;
import io.redisearch.SearchResult;
import io.redisearch.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class AttractionRedisRepositoryImpl implements AttractionRedisRepository {

    private final Client client;

    public AttractionRedisRepositoryImpl(@Qualifier("placeSearchClient") Client client) {
        this.client = client;
    }

    public List<AttractionSimpleResponse> findAll() {
        Query query = new Query("*")
                .limit(0, 1000); // 최대 1000개까지만 조회
        SearchResult result = client.search(query);

        return result.docs.stream()
                .map(AttractionSimpleResponse::toResponse)
                .collect(Collectors.toList());
    }

    public AttractionSimpleResponse findByContentId(Long contentId) {
        Document doc = client.getDocument("attraction:" + contentId);
        if (doc == null) return null;
        return AttractionSimpleResponse.toResponse(doc);
    }

    public List<AttractionSimpleResponse> findByCategory(String category) {
        String escaped = escape(category);
        Query query = new Query("@category_name:{" + escaped + "}");
        SearchResult result = client.search(query);
        return toResponseList(result);
    }

    public List<AttractionSimpleResponse> findByContentType(String typeName) {
        String escaped = escape(typeName);
        Query query = new Query("@content_type_name:{" + escaped + "}");
        SearchResult result = client.search(query);
        return toResponseList(result);
    }

    public List<AttractionSimpleResponse> findByAreaCode(String siGunGuLike) {
        String escaped = escape(siGunGuLike);
        Query query = new Query("@si_gun_gu_name:%" + escaped + "%");
        SearchResult result = client.search(query);
        return toResponseList(result);
    }

    public List<AttractionSimpleResponse> getNearBy(LocationSearchRequest locationRequest) {
        String geoQuery = String.format("@location:[%.6f %.6f %.1f km]",
                locationRequest.getLongitude(),
                locationRequest.getLatitude(),
                locationRequest.getRadiuskm());

        System.out.println("geoQuery: " + geoQuery);
        Query query = new Query(geoQuery).limit(0, 100); // 최대 100개
        SearchResult result = client.search(query);

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

//    public Optional<AttractionSimpleResponse> findAttractionById(String contentId) {
//        String key = PREFIX + contentId;
//        Map<Object, Object> data = placeRedisTemplate.opsForHash().entries(key);
//
//        if (data.isEmpty()) return Optional.empty();
//
//        try {
//            return Optional.of(
//                    new AttractionSimpleResponse(
//                            Long.parseLong(contentId),
//                            (String) data.getOrDefault("title", ""),
//                            Integer.parseInt((String) data.getOrDefault("content_type_id", "0")),
//                            Double.parseDouble(((String) data.getOrDefault("location", "0 0")).split(" ")[1]),
//                            Double.parseDouble(((String) data.getOrDefault("location", "0 0")).split(" ")[0]),
//                            Integer.parseInt((String) data.getOrDefault("area_code", "0")),
//                            Integer.parseInt((String) data.getOrDefault("si_gun_gu_code", "0")),
//                            Integer.parseInt((String) data.getOrDefault("map_level", "0")),
//                            (String) data.getOrDefault("tel", ""),
//                            (String) data.getOrDefault("addr1", ""),
//                            (String) data.getOrDefault("first_image1", ""),
//                            (String) data.getOrDefault("first_image2", "")
//                    )
//            );
//        } catch (Exception e) {
//            System.out.println("파씽 실패했는뎁쇼..?");
//            return Optional.empty();
//        }
//    }


}
