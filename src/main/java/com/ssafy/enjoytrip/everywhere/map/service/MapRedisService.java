package com.ssafy.enjoytrip.everywhere.map.service;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.mapper.AttractionMapper;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MapRedisService {

    private final AttractionRedisRepository attractionRedisRepository;
    private final AttractionMapper attractionMapper;
    private final RestTemplate restTemplate;

    /**
     * Redis에서 모든 장소 조회
     */
    public List<AttractionSimpleResponse> getAllAttractionsFromRedis() {
        return attractionRedisRepository.findAll().stream()
                .map(attractionMapper::toSimpleResponse)
                .toList();
    }

    /**
     * Redis에서 contentTypeId 기준 필터
     */
    public List<AttractionSimpleResponse> getByContentTypeFromRedis(String contentType) {
        return attractionRedisRepository.findByContentType(contentType).stream()
                .map(attractionMapper::toSimpleResponse)
                .toList();
    }

    /**
     * Redis에서 category 기준 필터
     */
    public List<AttractionSimpleResponse> getByCategoryFromRedis(String categoryCode) {
        return attractionRedisRepository.findByCategory(categoryCode).stream()
                .map(attractionMapper::toSimpleResponse)
                .toList();
    }

    /**
     * Redis에서 키워드 벡터 유사도 기반 검색
     */
    public List<AttractionSimpleResponse> searchByKeywordInRedis(String keyword) {
//        List<AttractionRedis> result = attractionRedisRepository.searchByKeywordEmbedding(keyword);

        Map<String, Object> payload = new HashMap<>();
        payload.put("keyword", "경복궁");
        payload.put("userType", "premium");
        payload.put("searchWeight", 0.8);
        List<Long> result = searchByDynamicJson(payload);
        System.out.println("RESULT!!!!!");
        System.out.println(result);

        return new ArrayList<>();
    }

    /**
     * Redis에서 지역 기반 필터
     */
    public List<AttractionSimpleResponse> getByRegionFromRedis(Integer areaCode, Integer siGunGuCode) {
        return attractionRedisRepository.findByRegion(areaCode, siGunGuCode).stream()
                .map(attractionMapper::toSimpleResponse)
                .toList();
    }
    public List<AttractionSimpleResponse> getByAreaFromRedis(Integer areaCode) {
        return attractionRedisRepository.findByAreaCode(areaCode).stream()
                .map(attractionMapper::toSimpleResponse)
                .toList();
    }

    public List<Long> searchByDynamicJson(Map<String, Object> dynamicPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(dynamicPayload, headers);

            ResponseEntity<Long[]> response = restTemplate.exchange(
                    "http://127.0.0.1:8000/search",  // ⬅️ Python API 주소
                    HttpMethod.POST,
                    request,
                    Long[].class
            );

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            System.out.println("❌ 오류: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
