package com.ssafy.enjoytrip.everywhere.ai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationRequest {
    private double latitude;
    private double longitude;
}
