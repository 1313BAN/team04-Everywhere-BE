package com.ssafy.enjoytrip.everywhere.route.dto.request;

import java.util.List;

public record RouteRequest (
        String title,
        String description,
        double longitude,
        double latitude,
        List<RouteSimpleRequest> attractions
) {
}
