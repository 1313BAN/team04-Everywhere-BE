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
}

