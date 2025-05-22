package com.ssafy.enjoytrip.everywhere.map.service;

import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionSimpleResponse;
import com.ssafy.enjoytrip.everywhere.map.dto.response.AttractionsResponse;
import com.ssafy.enjoytrip.everywhere.map.mapper.AttractionMapper;
import com.ssafy.enjoytrip.everywhere.map.repository.AttractionRepository;
import com.ssafy.enjoytrip.everywhere.map.util.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final AttractionRepository attractionRepository;
    private final AttractionMapper attractionMapper;

    public AttractionsResponse getAllAttractions() {
        return new AttractionsResponse(attractionRepository.findAll().stream()
                .map(attractionMapper::toSimpleResponse)
                .toList());
    }

    public AttractionsResponse getAttractionsByContentType(Integer contentTypeId) {
        List<AttractionSimpleResponse> attractions = attractionRepository.findByContentTypeId(contentTypeId)
                .stream()
                .map(attractionMapper::toSimpleResponse)
                .toList();
        return new AttractionsResponse(attractions);
    }

    public AttractionsResponse getAttractionsByBoundsWithZoom(String swLatLng, String neLatLng, int zoomLevel) {
        LatLng sw = LatLng.fromString(swLatLng);
        LatLng ne = LatLng.fromString(neLatLng);

        int limit;
        if (zoomLevel >= 15) limit = 300;
        else if (zoomLevel >= 12) limit = 200;
        else if (zoomLevel >= 10) limit = 100;
        else limit = 50;

        return new AttractionsResponse(
                attractionRepository.findWithinBoundsWithLimit(
                        sw.lat(), ne.lat(), sw.lng(), ne.lng(),
                        PageRequest.of(0, limit)
                )
        );
    }

}