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

    // TODO: attraction 필요 요소 넣기
    /**
    content_id bigint PK
    title varchar(255)
    content_type_id int
    area_code int
    si_gun_gu_code int
    first_image1 varchar(100)
    first_image2 varchar(100)
    map_level int
    latitude double
    longitude double
    tel varchar(20)
    addr1 varchar(100)
    addr2 varchar(100)
    homepage varchar(1000)
    overview varchar(10000)
    category varchar(10) **/

}

