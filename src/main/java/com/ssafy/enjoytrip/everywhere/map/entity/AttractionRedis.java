package com.ssafy.enjoytrip.everywhere.map.entity;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;


@Data
@Getter
public class AttractionRedis implements Serializable {

    private Long contentId;
    private String contentType;
    private String category;

    private float[] embedding;

    private String title;
    private String siGunGuName;

    private Integer areaCode;
    private Integer siGunGuCode;

    private String firstImage1;
    private String firstImage2;

    private Integer mapLevel;
    private Double latitude;
    private Double longitude;

    private String tel;
    private String addr1;
    private String addr2;

//    private String homepage;
//    private String overview;
}
