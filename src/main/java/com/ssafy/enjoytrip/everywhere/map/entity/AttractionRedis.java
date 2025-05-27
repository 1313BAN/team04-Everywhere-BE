package com.ssafy.enjoytrip.everywhere.map.entity;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;


@Data
@Getter
public class AttractionRedis implements Serializable {

    private Long contentId;
    private String title;

    private String contentTypeId;
    private String category;

    private Integer areaCode;
    private Integer siGunGuCode;

    private String firstImage1;

    private Integer mapLevel;
    private Double latitude;
    private Double longitude;

    private String tel;
    private String addr1;

}
