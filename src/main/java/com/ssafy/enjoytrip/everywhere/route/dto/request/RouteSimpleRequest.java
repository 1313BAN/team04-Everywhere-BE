package com.ssafy.enjoytrip.everywhere.route.dto.request;

public record RouteSimpleRequest(
        long contentId,
        String title,
        int contentTypeId,
        double latitude,
        double longitude,
        int sequence
) {
    @Override
    public String toString() {
        return "{'name'='" + title + '\'' +
                ", 'latitude'=" + latitude +
                ", 'longitude'=" + longitude +
                ", 'content_id='" + contentId +
                '}';
    }
}