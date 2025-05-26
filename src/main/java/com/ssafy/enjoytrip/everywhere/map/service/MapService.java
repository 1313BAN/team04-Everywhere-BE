package com.ssafy.enjoytrip.everywhere.map.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionRedisResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
import com.ssafy.enjoytrip.everywhere.map.mapper.AttractionMapper;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRepository;
import com.ssafy.enjoytrip.everywhere.map.util.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MapService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final AttractionRepository attractionRepository;
    private final AttractionMapper attractionMapper;

    public AttractionsResponse getAllAttractions() {
        return new AttractionsResponse(attractionRepository.findAll().stream()
                .map(attractionMapper::toSimpleResponse)
                .toList());
    }

    public AttractionsResponse getAttractionsByBoundsWithZoom(String swLatLng, String neLatLng, int zoomLevel) {
        LatLng sw = LatLng.fromString(swLatLng);
        LatLng ne = LatLng.fromString(neLatLng);

        return new AttractionsResponse(attractionRepository.findWithinBoundsWithLimit(
                sw.lat(), ne.lat(), sw.lng(), ne.lng(),
                PageRequest.of(0, selectLimit(zoomLevel))
        )
        );
    }

    public AttractionsResponse searchByKeyword(String keyword) {
        return new AttractionsResponse(attractionRepository.findByTitleContaining(keyword).stream()
                .map(attractionMapper::toSimpleResponse)
                .toList());
    }

    private static int selectLimit(int zoomLevel) {
        int limit;
        if (zoomLevel >= 15) limit = 300;
        else if (zoomLevel >= 12) limit = 200;
        else if (zoomLevel >= 10) limit = 100;
        else limit = 50;
        return limit;
    }

    public AttractionsResponse getAttractionsByBounds(String swLatLng, String neLatLng) {
        LatLng sw = LatLng.fromString(swLatLng);
        LatLng ne = LatLng.fromString(neLatLng);

        return new AttractionsResponse(
                attractionRepository.findWithinBounds(sw.lat(), ne.lat(), sw.lng(), ne.lng())
                        .stream()
                        .map(attractionMapper::toSimpleResponse)
                        .toList()
        );
    }

    public AttractionsResponse getAttractionsByCategory(String category) {
        return new AttractionsResponse(
                attractionRepository.findByCategory(category).stream()
                        .map(attractionMapper::toSimpleResponse)
                        .toList()
        );
    }

    /**
     *
     * vue 테스트를 위한 더미 값 반환
     */
    public AttractionsResponse getAllAttractionsFromCache() {
        Map<Object, Object> cached = redisTemplate.opsForHash().entries("attractions:hash");

        List<AttractionSimpleResponse> result = cached.values().stream()
                .map(value -> {
                    AttractionRedisResponse dto = (AttractionRedisResponse) value;
                    return new AttractionSimpleResponse(
                            dto.getContentId(),
                            dto.getTitle(),
                            0,
                            dto.getLatitude(),
                            dto.getLongitude(),
                            0, 0, 0,
                            null,
                            dto.getAddress(),
                            dto.getFirstImage(),
                            null,
                            dto.getCategory()
                    );
                })
                .toList();

        return new AttractionsResponse(result);
    }



}
