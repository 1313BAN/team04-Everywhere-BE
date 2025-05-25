package com.ssafy.enjoytrip.everywhere.ai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationSearchRequest {
    private double latitude;     // 위도
    private double longitude;    // 경도
    private double radiusKm;
}
