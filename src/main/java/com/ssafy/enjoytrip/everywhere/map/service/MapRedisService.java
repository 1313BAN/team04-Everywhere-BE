package com.ssafy.enjoytrip.everywhere.map.service;

import com.ssafy.enjoytrip.everywhere.map.dto.request.LocationSearchRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.request.NearAttractionRequest;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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
        return attractionRedisRepository.findNearBy(locationRequest);
    }

    public List<AttractionSimpleResponse> searchByKeywordEmbedding(String keyword) {
        return attractionRedisRepository.searchByKeywordEmbedding(keyword);
    }

    public List<AttractionSimpleResponse> findNearByLocationAndCategory(NearAttractionRequest request) {
        return attractionRedisRepository.findNearByLocationAndCategory(request);
    }

}
