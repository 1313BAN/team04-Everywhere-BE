package com.ssafy.enjoytrip.everywhere.map.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttractionRedisResponse {
    private Long contentId;
    private String title;
    private Double latitude;
    private Double longitude;
    private String address;
    private String firstImage;
    private String category;
}
