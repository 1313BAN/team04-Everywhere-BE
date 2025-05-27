package com.ssafy.enjoytrip.everywhere.map.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public class LocationSearchRequest {
    private double latitude;     // 위도
    private double longitude;    // 경도
    private double radiuskm;
}
