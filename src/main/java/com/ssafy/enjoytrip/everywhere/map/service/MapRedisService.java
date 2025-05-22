package com.ssafy.enjoytrip.everywhere.map.service;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.entity.AttractionRedis;
import com.ssafy.enjoytrip.everywhere.map.mapper.AttractionMapper;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapRedisService {

    private final AttractionRedisRepository attractionRedisRepository;
    private final AttractionMapper attractionMapper;

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
        List<AttractionRedis> result = attractionRedisRepository.searchByKeywordEmbedding(keyword);
        return result.stream()
                .map(attractionMapper::toSimpleResponse)
                .toList();
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
}
