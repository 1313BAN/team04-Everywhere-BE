package com.ssafy.enjoytrip.everywhere.map.util;

import com.ssafy.enjoytrip.everywhere.common.constants.ErrorCode;
import com.ssafy.enjoytrip.everywhere.common.exception.ApiException;

public record LatLng(double lat, double lng) {
    private static final String COMMA = ",";
    private static final int LENGTH = 2;

    public static LatLng fromString(String coordinate) {
        String[] parts = coordinate.split(COMMA);
        if (parts.length != LENGTH) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
        return new LatLng(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
    }
}

