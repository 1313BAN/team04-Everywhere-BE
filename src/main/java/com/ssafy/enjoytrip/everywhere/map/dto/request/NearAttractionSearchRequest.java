package com.ssafy.enjoytrip.everywhere.map.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NearAttractionSearchRequest {
    private double latitude;
    private double longitude;
    private String query;
}
