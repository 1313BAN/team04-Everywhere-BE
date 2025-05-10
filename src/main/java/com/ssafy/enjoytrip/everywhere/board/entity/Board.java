package com.ssafy.enjoytrip.everywhere.board.entity;

import com.ssafy.enjoytrip.everywhere.common.entity.BaseEntity;
import com.ssafy.enjoytrip.everywhere.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity writer;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "board_images", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    private int viewCount;
    private int likes;

    public void update(String title, String content, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }
}