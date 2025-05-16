package com.ssafy.enjoytrip.everywhere.map.dto.response;

public record AttractionSimpleResponse(
        Long no,
        Integer contentId,
        String title,
        Integer contentTypeId,
        Double latitude,
        Double longitude
) {}

