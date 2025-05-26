package com.ssafy.enjoytrip.everywhere.hotplace.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotplace {
    private String userId;
    private Long attractionId;
    private String title;
    private String overview;
}

