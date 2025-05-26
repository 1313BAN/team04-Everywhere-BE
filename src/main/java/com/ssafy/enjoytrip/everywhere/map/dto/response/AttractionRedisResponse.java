package com.ssafy.enjoytrip.everywhere.map.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttractionRedisResponse {
    private Long contentId;
    private String title;
    private Integer contentTypeId;
    private Double latitude;
    private Double longitude;
    private String address;
    private String firstImage;
    private String category;
}
