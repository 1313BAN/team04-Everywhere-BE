package com.ssafy.enjoytrip.everywhere.map.service;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
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

    public AttractionsResponse getAttractionsByContentType(Integer contentTypeId) {
        List<AttractionSimpleResponse> list = attractionRepository.findByContentTypeId(contentTypeId)
                .stream()
                .map(AttractionSimpleResponse::from)
                .toList();

        return new AttractionsResponse(list);
    }
}

