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
        String secondImage,
        String category
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
                attraction.getSecondImage(),
                attraction.getCategory()
        );
    }
    public static AttractionSimpleResponse toResponse(Document doc) {
        String location = getOrDefault(doc, "location", "0 0");
        String[] coords = location.split(" ");
        double longitude = safeParseDouble(coords[0], 0.0);
        double latitude = safeParseDouble(coords[1], 0.0);

        return new AttractionSimpleResponse(
                Long.parseLong(doc.getId().replace("attraction:", "")),
                getOrDefault(doc, "title", ""),
                0, // content_type_id 없으므로 기본값
                latitude,
                longitude,
                safeParseInt(doc.get("area_code"), 0),
                safeParseInt(doc.get("si_gun_gu_code"), 0),
                safeParseInt(doc.get("map_level"), 0),
                getOrDefault(doc, "tel", ""),
                getOrDefault(doc, "addr1", ""),
                getOrDefault(doc, "first_image2", ""),
                null,
                getOrDefault(doc, "category_name", "")
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