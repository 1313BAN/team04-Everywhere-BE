package com.ssafy.enjoytrip.everywhere.ai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LocationSearchRequest {
    private double latitude;     // 위도
    private double longitude;    // 경도
    private double radiuskm;
}
