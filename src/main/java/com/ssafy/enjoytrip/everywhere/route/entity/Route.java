package com.ssafy.enjoytrip.everywhere.route.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(nullable = false, length = 100)
    private String location;

    private Double latitude;
    private Double longitude;

    @Column(length = 50)
    private String distance;

    @Column(length = 255)
    private String category;
}
