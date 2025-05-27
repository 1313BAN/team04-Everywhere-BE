package com.ssafy.enjoytrip.everywhere.map.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NearAttractionRequest {
    private double latitude;
    private double longitude;
    private double radiuskm;
    private String query;
}
