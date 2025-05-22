package com.ssafy.enjoytrip.everywhere.map.dto.response;

import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;

public record AttractionSimpleResponse(
        long contentId,
        String title,
        int contentTypeId,
        double latitude,
        double longitude,
        int areaCode,
        int siGunGuCode,
        int mapLevel,
        String tel,
        String address,
        String firstImage,
        String secondImage
) {
    public static AttractionSimpleResponse from(Attraction attraction) {
        return new AttractionSimpleResponse(
                attraction.getContentId(),
                attraction.getTitle(),
                attraction.getContentTypeId(),
                attraction.getLatitude(),
                attraction.getLongitude(),
                attraction.getAreaCode(),
                attraction.getSiGunGuCode(),
                attraction.getMapLevel(),
                attraction.getTel(),
                attraction.getAddress(),
                attraction.getFirstImage(),
                attraction.getSecondImage()
        );
    }
}