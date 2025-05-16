package com.ssafy.enjoytrip.everywhere.map.service;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.mapper.AttractionMapper;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final AttractionRepository attractionRepository;
    private final AttractionMapper attractionMapper;

    public List<AttractionSimpleResponse> getAllAttractions() {
        return attractionRepository.findAll().stream()
                .map(attractionMapper::toSimpleResponse)
                .toList();
    }

    public List<AttractionSimpleResponse> getAttractionsByType(Integer type) {
        return attractionRepository.findByContentTypeId(type).stream()
                .map(p -> new AttractionSimpleResponse(
                        p.getNo(), p.getContentId(), p.getTitle(),
                        p.getContentTypeId(), p.getLatitude(), p.getLongitude()))
                .toList();
    }
}

