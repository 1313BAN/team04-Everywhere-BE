package com.ssafy.enjoytrip.everywhere.map.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attractions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private Integer contentId;

    private String title;

    private Integer contentTypeId;

    private Double latitude;

    private Double longitude;
}

