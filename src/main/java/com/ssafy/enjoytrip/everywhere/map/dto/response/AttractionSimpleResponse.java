package com.ssafy.enjoytrip.everywhere.map.dto.response;

import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;

public record AttractionSimpleResponse(
        Long contentId,
        String title,
        Integer contentTypeId,
        Double latitude,
        Double longitude
) {
    public static AttractionSimpleResponse from(Attraction attraction) {
        return new AttractionSimpleResponse(
                attraction.getContentId(),
                attraction.getTitle(),
                attraction.getContentTypeId(),
                attraction.getLatitude(),
                attraction.getLongitude()
        );
    }
}