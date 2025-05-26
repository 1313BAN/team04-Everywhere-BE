package com.ssafy.enjoytrip.everywhere.map.service;

import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationRequest;
import com.ssafy.enjoytrip.everywhere.ai.dto.request.LocationSearchRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
import com.ssafy.enjoytrip.everywhere.map.entity.AttractionRedis;
import com.ssafy.enjoytrip.everywhere.map.mapper.AttractionMapper;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRedisRepository;
import io.redisearch.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapRedisService {

    private final AttractionRedisRepository attractionRedisRepository;

    public List<AttractionSimpleResponse> getAll() {
        return attractionRedisRepository.findAll();
    }

    public List<AttractionSimpleResponse> getByContentIds(List<Long> contentIds) {
        return attractionRedisRepository.findByContentIds(contentIds);
    }

    public AttractionSimpleResponse getByContentId(long contentId) {
        return attractionRedisRepository.findByContentId(contentId);
    }

    public List<AttractionSimpleResponse> getByCategory(String categoryName) {
        return attractionRedisRepository.findByCategory(categoryName);
    }

    public List<AttractionSimpleResponse> getByContentType(String typeName) {
        return attractionRedisRepository.findByContentType(typeName);
    }

    public List<AttractionSimpleResponse> getByAreaCode(String areaName) {
        return attractionRedisRepository.findByAreaCode(areaName);
    }

    public List<AttractionSimpleResponse> getNearBy(LocationSearchRequest locationRequest) {
        return attractionRedisRepository.getNearBy(locationRequest);
    }
}
