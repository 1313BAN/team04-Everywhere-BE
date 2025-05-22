package com.ssafy.enjoytrip.everywhere.map.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attractions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attraction {

    @Id
    @Column(name = "content_id")
    private Long contentId;

    private String title;

    @Column(name = "content_type_id")
    private Integer contentTypeId;

    private Double latitude;

    private Double longitude;

    private Integer areaCode;
    private Integer siGunGuCode;
    private Integer mapLevel;
    private String tel;
    @Column(name = "addr1")
    private String address;

    /**
    area_code int
    si_gun_gu_code int
    first_image1 varchar(100)
    first_image2 varchar(100)
    map_level int
    tel varchar(20)
    addr1 varchar(100)
    addr2 varchar(100)
    category varchar(10) **/

}

