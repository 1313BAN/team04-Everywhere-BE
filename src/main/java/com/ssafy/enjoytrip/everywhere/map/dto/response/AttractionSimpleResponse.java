package com.ssafy.enjoytrip.everywhere.map.dto.response;

import com.ssafy.enjoytrip.everywhere.map.entity.Attraction;
import io.redisearch.Document;

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
    public static AttractionSimpleResponse toResponse(Document doc) {
        return new AttractionSimpleResponse(
                Long.parseLong(doc.getId().replace("attraction:", "")),
                getOrDefault(doc, "title", ""),
                safeParseInt(doc.get("content_type_id"), 0),
                safeParseDouble(doc.get("latitude"), 0.0),
                safeParseDouble(doc.get("longitude"), 0.0),
                safeParseInt(doc.get("area_code"), 0),
                safeParseInt(doc.get("si_gun_gu_code"), 0),
                safeParseInt(doc.get("map_level"), 0),
                getOrDefault(doc, "tel", ""),
                getOrDefault(doc, "address", ""),
                getOrDefault(doc, "first_image", ""),
                getOrDefault(doc, "second_image", "")
        );
    }

    private static int safeParseInt(Object value, int defaultValue) {
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static double safeParseDouble(Object value, double defaultValue) {
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static String getOrDefault(Document doc, String key, String defaultValue) {
        Object value = doc.get(key);
        return value != null ? value.toString() : defaultValue;
    }
}